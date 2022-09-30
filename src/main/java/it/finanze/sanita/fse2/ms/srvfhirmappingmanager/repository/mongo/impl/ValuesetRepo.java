package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.mongo.impl;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Logs.ERROR_UNABLE_FIND_DELETIONS;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Logs.ERROR_UNABLE_FIND_INSERTIONS;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.ValuesetETY.FIELD_ID;
import static org.springframework.data.mongodb.core.BulkOperations.BulkMode.UNORDERED;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoException;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IValuesetRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.ValuesetETY;


@Repository
public class ValuesetRepo implements IValuesetRepo {

	@Autowired
	private MongoTemplate mongo;

	@Override
	public boolean isDocumentInserted(String name) throws OperationException {
		boolean res;
		Query query = new Query();
		query.addCriteria(where("name_valueset").is(name).and("deleted").is(false));
		try {
			res = mongo.exists(query, ValuesetETY.class);
		} catch(MongoException e) {
			throw new OperationException("Unable to verify if given document is inserted" , e);
		}
		return res;
	}

	@Override
	public ValuesetETY findDocByName(String name) throws OperationException {
		ValuesetETY out;
		Query query = new Query();
		query.addCriteria(Criteria.where("name_valueset").is(name).and("deleted").is(false));
		try {
			out = mongo.findOne(query, ValuesetETY.class);
		} catch (MongoException e) {
			throw new OperationException("Unable to retrieve valueset by name", e);
		}
		return out;
	}

	@Override
	public ValuesetETY insertDocByName(ValuesetETY entity) throws OperationException {
		ValuesetETY out;
		try {
			out = mongo.insert(entity);
		} catch (MongoException e) {
			throw new OperationException("Unable to insert valueset by name", e);
		}
		return out;
	}

	@Override
	public void insertAll(List<ValuesetETY> entities) throws OperationException {
		try {
			mongo.insertAll(entities);
		} catch (MongoException e) {
			throw new OperationException("Unable to insert valueset by name", e);
		}
	}


	@Override
	public ValuesetETY updateDocByName(ValuesetETY current, ValuesetETY newest) throws OperationException {
		BulkOperations ops = mongo.bulkOps(UNORDERED, ValuesetETY.class);
		Query query = new Query();
		query.addCriteria(where(FIELD_ID).is(new ObjectId(current.getId())));
		query.addCriteria(where("deleted").is(false));

		Update update = new Update();
		update.set(FIELD_LAST_UPDATE, new Date());
		update.set("deleted", true);
		ops.updateOne(query, update);
		ops.insert(newest);
		try {
			ops.execute();
		} catch (MongoException e) {
			throw new OperationException("Unable to update values by name" , e);
		}
		return newest;
	}

	@Override
	public ValuesetETY deleteDocByName(String name) throws OperationException {
		ValuesetETY out;
		Query query = new Query();
		query.addCriteria(where("name_valueset").is(name));
		query.addCriteria(where("deleted").is(false));

		Update update = new Update();
		update.set(FIELD_LAST_UPDATE, new Date());
		update.set("deleted", true);
		out = findDocByName(name);
		try {
			mongo.updateFirst(query, update, ValuesetETY.class);
		} catch (MongoException e) {
			throw new OperationException("Unable to insert valueset by name", e);
		}
		return out;
	}

	@Override
	public ValuesetETY findDocById(String id) throws OperationException {
		ValuesetETY out;
		Query query = new Query();
		query.addCriteria(Criteria.where(FIELD_ID).is(id).and("deleted").is(false));
		try {
			out = mongo.findOne(query, ValuesetETY.class);
		} catch (MongoException e) {
			throw new OperationException("Unable to retrieve valueset by id", e);
		}
		return out;
	}

	/**
	 * Retrieves the latest insertions according to the given timeframe
	 *
	 * @param lastUpdate The timeframe to consider while calculating
	 * @return The missing insertions
	 * @throws OperationException If a data-layer error occurs
	 */
	@Override
	public List<ValuesetETY> getInsertions(Date lastUpdate) throws OperationException {
		List<ValuesetETY> objects;
		Query q = Query.query(Criteria.where(FIELD_INSERTION_DATE).gt(lastUpdate).and("deleted").ne(true));
		try {
			objects = mongo.find(q, ValuesetETY.class);
		} catch (MongoException e) {
			throw new OperationException(ERROR_UNABLE_FIND_INSERTIONS, e);
		}
		return objects;
	}

	/**
	 * Retrieves the latest deletions according to the given timeframe
	 *
	 * @param lastUpdate The timeframe to consider while calculating
	 * @return The missing deletions
	 * @throws OperationException If a data-layer error occurs
	 */
	@Override
	public List<ValuesetETY> getDeletions(Date lastUpdate) throws OperationException {
		List<ValuesetETY> objects;
		Query q = Query.query(Criteria.where(FIELD_LAST_UPDATE).gt(lastUpdate).and(FIELD_INSERTION_DATE).lte(lastUpdate).and("deleted").is(true));
		try {
			objects = mongo.find(q, ValuesetETY.class);
		} catch (MongoException e) {
			throw new OperationException(ERROR_UNABLE_FIND_DELETIONS, e);
		}
		return objects;
	}

	/**
	 * Retrieves all the not-deleted extensions with their documents data
	 *
	 * @return Any available schema
	 * @throws OperationException If a data-layer error occurs
	 */
	@Override
	public List<ValuesetETY> getEveryActiveDocument() throws OperationException {
		List<ValuesetETY> objects;
		Query q = query(where("deleted").ne(true));
		try {
			objects = mongo.find(q, ValuesetETY.class);
		} catch (MongoException e) {
			throw new OperationException("Unable to retrieve every available valueset", e);
		}
		return objects;
	}


	@Override
	public List<String> isDocumentsInserted(List<String> names) throws OperationException {
		List<String> filesAlreadyPresent = new ArrayList<>();
		try {
			Query query = new Query();
			query.addCriteria(where("name_valueset").in(names).and("deleted").is(false));
			List<ValuesetETY> valuesetFounded = mongo.find(query, ValuesetETY.class);
			if(!valuesetFounded.isEmpty()) {
				filesAlreadyPresent = valuesetFounded.stream().map(e->e.getFilenameValueset()).collect(Collectors.toList());
			}
		} catch(MongoException e) {
			throw new OperationException("Unable to verify if given document is inserted" , e);
		}
		return filesAlreadyPresent;
	}

}
