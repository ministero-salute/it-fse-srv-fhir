/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.mongo.impl;


import com.mongodb.MongoException;
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

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Logs.*;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY.FIELD_TEMPLATE_ID_ROOT;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY.FIELD_URI;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 *	Transform repository.
 */
@Slf4j
@Repository
public class TransformRepo implements ITransformRepo, Serializable {

	@Autowired
	private MongoTemplate mongo;
	
	@Override
	public TransformETY insert(TransformETY entity) throws OperationException {
		TransformETY out;
		try {
			out = mongo.insert(entity);
		} catch(MongoException e) {
			log.error(ERR_REP_INS_DOCS_BY_URI, e);
			throw new OperationException(ERR_REP_INS_DOCS_BY_URI, e);
		}
		return out;
	}

	@Override
	public List<TransformETY> remove(final String uri) throws OperationException {
		List<TransformETY> q;
		// Prepare find query
		Query query = query(where(FIELD_URI).is(uri).and(FIELD_DELETED).ne(true));
		// Prepare update query
		Update update = new Update();
		update.set(FIELD_DELETED, true);
		update.set(FIELD_LAST_UPDATE, new Date());
		try {
			q = mongo.find(query, TransformETY.class);
			mongo.updateMulti(query, update, TransformETY.class);
		} catch (MongoException e) {
			log.error(ERR_REP_DEL_DOCS_BY_URI, e);
			throw new OperationException(ERR_REP_DEL_DOCS_BY_URI, e);
		}
		return q;
	}

	@Override
	public TransformETY findByUri(String uri) throws OperationException {
		TransformETY out;
		// Search by uri and version
		Query q = query(where(FIELD_URI).is(uri).and(FIELD_DELETED).ne(true));
		// Sort by insertion
		q = q.with(Sort.by(Direction.DESC, FIELD_INSERTION_DATE));
		try {
			out = mongo.findOne(q, TransformETY.class);
		} catch (MongoException e) {
			log.error(ERR_REP_FIND_BY_URI, e);
			throw new OperationException(ERR_REP_FIND_BY_URI, e);
		}
		return out;
	}

	@Override
	public TransformETY findByTemplateIdRoot(String templateIdRoot) throws OperationException {
		TransformETY out;
		// Search by uri and version
		Query q = query(where(FIELD_TEMPLATE_ID_ROOT).is(templateIdRoot).and(FIELD_DELETED).ne(true));
		try {
			out = mongo.findOne(q, TransformETY.class);
		} catch (MongoException e) {
			log.error(ERR_REP_FIND_BY_ROOT, e);
			throw new OperationException(ERR_REP_FIND_BY_ROOT, e);
		}
		return out;
	}

	@Override
	public List<TransformETY> findByUriAndDeleted(String uri, boolean deleted) throws OperationException {
		List<TransformETY> entities;
		// Search by template id
		Query q = query(where(FIELD_URI).is(uri));
		// Check if deleted are not allowed
		if(!deleted) q.addCriteria(where(FIELD_DELETED).ne(true));
		// Sort by insertion
		q = q.with(Sort.by(Direction.DESC, FIELD_INSERTION_DATE));
		try {
			entities = mongo.find(q, TransformETY.class);
		} catch (MongoException e) {
			log.error(ERR_REP_FIND_BY_URI, e);
			throw new OperationException(ERR_REP_FIND_BY_URI, e);
		}
		return entities;
	}


	@Override
	public List<TransformETY> findAll() throws OperationException {
		try {
			return mongo.findAll(TransformETY.class);
		} catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(ERROR_FIND_ALL_TRANSFORM, e);
		} catch (Exception ex) {
			log.error(ERROR_FIND_ALL_TRANSFORM + getClass(), ex);
			throw new BusinessException(ERROR_FIND_ALL_TRANSFORM + getClass(), ex);
		}
	}

	@Override
	public TransformETY findById(String id) throws OperationException {
		TransformETY out;
		Query q = query(where(FIELD_ID).is(id).and(FIELD_DELETED).ne(true));
		try {
			out = mongo.findOne(q, TransformETY.class);
		} catch (MongoException e) {
			log.error(ERR_REP_FIND_BY_ID, e);
			throw new OperationException(ERR_REP_FIND_BY_ID, e);
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
	public List<TransformETY> getInsertions(Date lastUpdate) throws OperationException {
		// Working var
		List<TransformETY> objects;
		// Create query
		Query q = query(
				where(FIELD_INSERTION_DATE).gt(lastUpdate).and(FIELD_DELETED).ne(true)
		);
		try {
			// Execute
			objects = mongo.find(q, TransformETY.class);
		} catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
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
	public List<TransformETY> getDeletions(Date lastUpdate) throws OperationException {
		// Working var
		List<TransformETY> objects;
		// Create query
		Query q = query(
				where(FIELD_LAST_UPDATE).gt(lastUpdate)
						.and(FIELD_INSERTION_DATE).lte(lastUpdate)
						.and(FIELD_DELETED).is(true)
		);
		try {
			// Execute
			objects = mongo.find(q, TransformETY.class);
		} catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(ERROR_UNABLE_FIND_DELETIONS, e);
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
		Query q = query(where(FIELD_DELETED).ne(true));
		try {
			// Execute
			objects = mongo.find(q, TransformETY.class);
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
		Query q = query(where(FIELD_DELETED).ne(true));
		try {
			// Execute count
			size = mongo.count(q, TransformETY.class);
		}catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(ERR_REP_COUNT_ACTIVE_DOC, e);
		}
		return size;
	}
}
