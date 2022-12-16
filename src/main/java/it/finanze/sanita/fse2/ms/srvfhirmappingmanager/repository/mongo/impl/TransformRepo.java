/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.mongo.impl;


import com.mongodb.MongoException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.ITransformRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY.FIELD_TEMPLATE_ID_ROOT;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

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
	public List<TransformETY> remove(final String templateIdRoot) throws OperationException {
		try {
			Query query = query(where(Constants.App.TEMPLATE_ID_ROOT).is(templateIdRoot).and(Constants.App.DELETED).ne(true));
			Update update = new Update();
			update.set(Constants.App.DELETED, true);
			update.set(FIELD_LAST_UPDATE, new Date());

			List<TransformETY> list = mongoTemplate.find(query, TransformETY.class);
			mongoTemplate.updateMulti(query, update, TransformETY.class);
			return list;
		} catch (MongoException e) {
			log.error(Constants.Logs.ERROR_DELETING_ETY, e);
			throw new OperationException(Constants.Logs.ERROR_DELETING_ETY, e);
		} catch (Exception ex) {
			log.error(Constants.Logs.ERROR_DELETING_ETY + getClass(), ex);
			throw new BusinessException(Constants.Logs.ERROR_DELETING_ETY + getClass(), ex);
		}
	}

	@Override
	public TransformETY findByTemplateIdRoot(String templateIdRoot) throws OperationException {
		try {
			// Search by template id
			Query q = query(where(FIELD_TEMPLATE_ID_ROOT).is(templateIdRoot).and(FIELD_DELETED).ne(true));
			// Sort by insertion
			q = q.with(Sort.by(Direction.DESC, FIELD_INSERTION_DATE));
			return mongoTemplate.findOne(q, TransformETY.class);
		} catch (MongoException e) {
			log.error(Constants.Logs.ERROR_FIND_TRANSFORM, e);
			throw new OperationException(Constants.Logs.ERROR_FIND_TRANSFORM, e);
		}
	}
	
	@Override
	public List<TransformETY> findByTemplateIdRootAndDeleted(String templateIdRoot, boolean deleted) throws OperationException {
		List<TransformETY> entities;
		// Search by template id
		Query q = query(where(FIELD_TEMPLATE_ID_ROOT).is(templateIdRoot));
		// Check if deleted are not allowed
		if(!deleted) q.addCriteria(where(FIELD_DELETED).ne(true));
		// Sort by insertion
		q = q.with(Sort.by(Direction.DESC, FIELD_INSERTION_DATE));
		try {
			entities = mongoTemplate.find(q, TransformETY.class); 
		} catch (MongoException e) {
			log.error(Constants.Logs.ERROR_FIND_TRANSFORM, e);
			throw new OperationException(Constants.Logs.ERROR_FIND_TRANSFORM, e);
		} catch (Exception ex) {
			log.error(Constants.Logs.ERROR_UPDATING_TRANSFORM + getClass(), ex);
			throw new BusinessException(Constants.Logs.ERROR_UPDATING_TRANSFORM + getClass(), ex);
		}
		return entities;
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
			return mongoTemplate.findOne(query(where(Constants.App.MONGO_ID).is(id)
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
		Query q = query(
				where(FIELD_INSERTION_DATE).gt(lastUpdate).and(Constants.App.DELETED).ne(true)
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
		Query q = query(
				where(FIELD_LAST_UPDATE).gt(lastUpdate)
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
		Query q = query(where(Constants.App.DELETED).ne(true));
		try {
			// Execute
			objects = mongoTemplate.find(q, TransformETY.class);
		} catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException("Unable to retrieve every available extension with their documents", e);
		}
		return objects;
	}

	/**
	 * Count all the not-deleted extensions items
	 *
	 * @return Number of active documents
	 * @throws OperationException If a data-layer error occurs
	 */
	@Override
	public long getActiveDocumentCount() throws OperationException {
		// Working var
		long size;
		// Create query
		Query q = query(where(Constants.App.DELETED).ne(true));
		try {
			// Execute count
			size = mongoTemplate.count(q, TransformETY.class);
		}catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(Constants.Logs.ERR_REP_COUNT_ACTIVE_DOC, e);
		}
		return size;
	}
}
