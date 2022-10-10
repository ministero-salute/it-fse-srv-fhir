package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.mongo.impl;


import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.ITransformRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *	Transform repository.
 */
@Slf4j
@Repository
public class TransformRepo implements ITransformRepo, Serializable {
	
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8314433348647769361L; 
	
	@Autowired
	private transient MongoTemplate mongoTemplate;
	
	@Override
	public TransformETY insert(TransformETY ety) throws OperationException {
		try {
			return mongoTemplate.insert(ety);
		} catch(MongoException e) {
			log.error(Constants.Logs.ERROR_INSERTING_ETY, e);
			throw new OperationException(Constants.Logs.ERROR_INSERTING_ETY, e);
		} catch(Exception ex) {
			log.error(Constants.Logs.ERROR_INSERTING_ETY, ex);
			throw new BusinessException(Constants.Logs.ERROR_INSERTING_ETY, ex);
		}
	}

	@Override
	public boolean remove(final String templateIdRoot, final String version) throws OperationException {
		try {
			Query query = Query.query(Criteria.where(Constants.App.TEMPLATE_ID_ROOT).is(templateIdRoot)
					.and(Constants.App.VERSION).is(version));

			// Template ID Root and Version uniquely determine a transform, we can use findOne
			// and take the first element
			TransformETY transformETY = mongoTemplate.findOne(query, TransformETY.class);

			if (transformETY != null) {
				Update update = new Update();
				update.set(Constants.App.DELETED, true);
				update.set(FIELD_LAST_UPDATE, new Date());

				UpdateResult result = mongoTemplate.updateFirst(
						Query.query(Criteria.where(Constants.App.TEMPLATE_ID_ROOT).is(transformETY.getTemplateIdRoot())
								.and(Constants.App.VERSION).is(transformETY.getVersion())
								.and(Constants.App.DELETED).ne(true)),
						update, TransformETY.class);

				return result.getModifiedCount() > 0;
			}
			return false;

		} catch (MongoException e) {
			log.error(Constants.Logs.ERROR_DELETING_ETY, e);
			throw new OperationException(Constants.Logs.ERROR_DELETING_ETY, e);
		} catch (Exception ex) {
			log.error(Constants.Logs.ERROR_DELETING_ETY + getClass(), ex);
			throw new BusinessException(Constants.Logs.ERROR_DELETING_ETY + getClass(), ex);
		}
	}

	@Override
	public TransformETY findByTemplateIdRootAndVersion(String templateIdRoot, String version) throws OperationException {
		try {
			return mongoTemplate.findOne(Query.query(Criteria.where(Constants.App.TEMPLATE_ID_ROOT).is(templateIdRoot)
							.and(Constants.App.VERSION).is(version).and(Constants.App.DELETED).ne(true)),
					TransformETY.class);
		} catch (MongoException e) {
			log.error(Constants.Logs.ERROR_FIND_TRANSFORM, e);
			throw new OperationException(Constants.Logs.ERROR_FIND_TRANSFORM, e);
		} catch (Exception ex) {
			log.error(Constants.Logs.ERROR_UPDATING_TRANSFORM + getClass(), ex);
			throw new BusinessException(Constants.Logs.ERROR_UPDATING_TRANSFORM + getClass(), ex);
		}
	}

	@Override
	public TransformETY findByTemplateIdRoot(String templateIdRoot) throws OperationException {
		try {
			SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, Constants.App.INSERTION_DATE);
			MatchOperation matchOperation = Aggregation.match(Criteria.where(Constants.App.TEMPLATE_ID_ROOT).is(templateIdRoot).and(Constants.App.DELETED).ne(true));
			LimitOperation limitOperation = Aggregation.limit(1);

			TypedAggregation<TransformETY> typedAggregation = new TypedAggregation<>(TransformETY.class, matchOperation, sortOperation, limitOperation);

			return mongoTemplate.aggregate(typedAggregation, TransformETY.class).getUniqueMappedResult();
		} catch (MongoException e) {
			log.error(Constants.Logs.ERROR_FIND_TRANSFORM, e);
			throw new OperationException(Constants.Logs.ERROR_FIND_TRANSFORM, e);
		} catch (Exception ex) {
			log.error(Constants.Logs.ERROR_UPDATING_TRANSFORM + getClass(), ex);
			throw new BusinessException(Constants.Logs.ERROR_UPDATING_TRANSFORM + getClass(), ex);
		}
	}

	@Override
	public List<TransformETY> findAll() throws OperationException {
		try {
			return mongoTemplate.findAll(TransformETY.class);
		} catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(Constants.Logs.ERROR_FIND_ALL_TRANSFORM, e);
		} catch (Exception ex) {
			log.error(Constants.Logs.ERROR_FIND_ALL_TRANSFORM + getClass(), ex);
			throw new BusinessException(Constants.Logs.ERROR_FIND_ALL_TRANSFORM + getClass(), ex);
		}
	}

	@Override
	public TransformETY findById(String id) throws OperationException {
		try {
			return mongoTemplate.findOne(Query.query(Criteria.where(Constants.App.MONGO_ID).is(id)
							.and(Constants.App.DELETED).ne(true)),
					TransformETY.class);
		} catch (MongoException e) {
			log.error(Constants.Logs.ERROR_FIND_TRANSFORM, e);
			throw new OperationException(Constants.Logs.ERROR_FIND_TRANSFORM, e);
		} catch (Exception ex) {
			log.error(Constants.Logs.ERROR_UPDATING_TRANSFORM + getClass(), ex);
			throw new BusinessException(Constants.Logs.ERROR_UPDATING_TRANSFORM + getClass(), ex);
		}
	}


	/**
	 * Retrieves the latest insertions according to the given timeframe
	 *
	 * @param lastUpdate The timeframe to consider while calculating
	 * @return The missing insertions
	 * @throws OperationException If a data-layer error occurs
	 */
	@Override
	public List<TransformETY> getInsertions(Date lastUpdate) throws OperationException {
		// Working var
		List<TransformETY> objects;
		// Create query
		Query q = Query.query(
				Criteria.where(FIELD_INSERTION_DATE).gt(lastUpdate).and(Constants.App.DELETED).ne(true)
		);
		try {
			// Execute
			objects = mongoTemplate.find(q, TransformETY.class);
		} catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(Constants.Logs.ERROR_UNABLE_FIND_INSERTIONS, e);
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
	public List<TransformETY> getDeletions(Date lastUpdate) throws OperationException {
		// Working var
		List<TransformETY> objects;
		// Create query
		Query q = Query.query(
				Criteria.where(FIELD_LAST_UPDATE).gt(lastUpdate)
						.and(FIELD_INSERTION_DATE).lte(lastUpdate)
						.and(Constants.App.DELETED).is(true)
		);
		try {
			// Execute
			objects = mongoTemplate.find(q, TransformETY.class);
		} catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(Constants.Logs.ERROR_UNABLE_FIND_DELETIONS, e);
		}
		return objects;
	}

	/**
	 * Retrieves all the not-deleted extensions with their data
	 *
	 * @return Any available transform
	 * @throws OperationException If a data-layer error occurs
	 */
	@Override
	public List<TransformETY> getEveryActiveDocument() throws OperationException {
		// Working var
		List<TransformETY> objects;
		// Create query
		Query q = Query.query(Criteria.where(Constants.App.DELETED).ne(true));
		try {
			// Execute
			objects = mongoTemplate.find(q, TransformETY.class);
		} catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException("Unable to retrieve every available extension with their documents", e);
		}
		return objects;
	}
}
