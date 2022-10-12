package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.mongo.impl;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IXslTransformRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Riccardo Bonesi
 *
 *	XSL Transform repository.
 */
@Slf4j
@Repository
public class XslTransformRepo implements IXslTransformRepo, Serializable {
	
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8314433348647769361L; 
	
	@Autowired
	private transient MongoTemplate mongoTemplate;
	
	@Override
	public XslTransformETY insert(XslTransformETY ety) throws OperationException {
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
                    .and(Constants.App.VERSION).is(version).and(Constants.App.DELETED).ne(true));

            Update update = new Update();
            update.set(Constants.App.DELETED, true);
            update.set(FIELD_LAST_UPDATE, new Date());
            
            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, XslTransformETY.class);
            return updateResult.getModifiedCount() > 0;
        } catch (MongoException e) {
            log.error(Constants.Logs.ERROR_DELETING_ETY, e);
            throw new OperationException(Constants.Logs.ERROR_DELETING_ETY, e);
        } catch (Exception ex) {
            log.error(Constants.Logs.ERROR_DELETING_ETY + getClass(), ex);
            throw new BusinessException(Constants.Logs.ERROR_DELETING_ETY + getClass(), ex);
        }

    }
	
    @Override
    public XslTransformETY findByTemplateIdRootAndVersion(String templateIdRoot, String version) throws OperationException {

        try {
            return mongoTemplate.findOne(Query.query(Criteria.where(Constants.App.TEMPLATE_ID_ROOT).is(templateIdRoot)
                    .and(Constants.App.VERSION).is(version).and(Constants.App.DELETED).ne(true)), XslTransformETY.class);
       
        } catch (MongoException e) {
            log.error(Constants.Logs.ERROR_FIND_XSL_TRANSFORM, e);
            throw new OperationException(Constants.Logs.ERROR_FIND_XSL_TRANSFORM, e);
        } catch (Exception ex) {
            log.error(Constants.Logs.ERROR_UPDATING_XSL_TRANSFORM + getClass(), ex);
            throw new BusinessException(Constants.Logs.ERROR_UPDATING_XSL_TRANSFORM + getClass(), ex);
        }
    }

    @Override
    public XslTransformETY findById(String id) throws OperationException {
        XslTransformETY object = null;

        try {
            // Execute
            object =  mongoTemplate.findOne(Query.query(Criteria.where(Constants.App.MONGO_ID).is(new ObjectId(id)).and(Constants.App.DELETED).ne(true)), XslTransformETY.class);
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException(Constants.Logs.ERROR_FIND_XSL_TRANSFORM, e);
        } catch (Exception ex) {
            log.error(Constants.Logs.ERROR_FIND_XSL_TRANSFORM + getClass(), ex);
            throw new BusinessException(Constants.Logs.ERROR_FIND_XSL_TRANSFORM + getClass(), ex);
        }

        return object;
    }

	
	@Override
	public List<XslTransformETY> findAll() throws OperationException {

        List<XslTransformETY> etyList = new ArrayList<>();

        try {
            etyList = mongoTemplate.findAll(XslTransformETY.class);
            
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException(Constants.Logs.ERROR_FIND_XSL_TRANSFORM, e);
        } catch (Exception ex) {
            log.error(Constants.Logs.ERROR_FIND_XSL_TRANSFORM + getClass(), ex);
            throw new BusinessException(Constants.Logs.ERROR_FIND_XSL_TRANSFORM + getClass(), ex);
        }

				
		return etyList.stream()
				.filter(i -> !i.isDeleted())
		        .collect(Collectors.toList()); 
	}

	@Override
	public void insertAll(List<XslTransformETY> etys) throws OperationException {
		try {
			mongoTemplate.insertAll(etys);
		} catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException(Constants.Logs.ERROR_INSERTING_ETY, e);
        } catch(Exception ex) {
			log.error(Constants.Logs.ERROR_INSERTING_ETY + getClass() , ex);
			throw new BusinessException(Constants.Logs.ERROR_INSERTING_ETY + getClass() , ex);
		}
	}


	@Override
	public boolean existByTemplateIdRoot(final String templateIdRoot) throws OperationException {
		boolean output = false;
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where(Constants.App.TEMPLATE_ID_ROOT).is(templateIdRoot));
			query.addCriteria(Criteria.where(Constants.App.DELETED).ne(true)); 
			output = mongoTemplate.exists(query, XslTransformETY.class);
		} catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException(Constants.Logs.ERROR_EXECUTE_EXIST_VERSION_QUERY, e);
        } catch(Exception ex) {
			log.error(Constants.Logs.ERROR_EXECUTE_EXIST_VERSION_QUERY + getClass() , ex);
			throw new BusinessException(Constants.Logs.ERROR_EXECUTE_EXIST_VERSION_QUERY + getClass(), ex);
		}
		return output;
	}

	/**
     * Retrieves the latest insertions according to the given timeframe
     *
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing insertions
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public List<XslTransformETY> getInsertions(Date lastUpdate) throws OperationException {
        // Working var
        List<XslTransformETY> objects;
        // Create query
        Query q = Query.query(
            Criteria.where(FIELD_INSERTION_DATE).gt(lastUpdate).and(Constants.App.DELETED).ne(true)
        );
        try {
            // Execute
            objects = mongoTemplate.find(q, XslTransformETY.class);
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
    public List<XslTransformETY> getDeletions(Date lastUpdate) throws OperationException {
        // Working var
        List<XslTransformETY> objects;
        // Create query
        Query q = Query.query(
            Criteria.where(FIELD_LAST_UPDATE).gt(lastUpdate)
                .and(FIELD_INSERTION_DATE).lte(lastUpdate)
                .and(Constants.App.DELETED).is(true)
        );
        try {
            // Execute
            objects = mongoTemplate.find(q, XslTransformETY.class);
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException(Constants.Logs.ERROR_UNABLE_FIND_DELETIONS, e);
        }
        return objects;
    }

	/**
     * Retrieves all the not-deleted extensions with their data
     *
     * @return Any available xslTransform
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public List<XslTransformETY> getEveryActiveDocument() throws OperationException {
        // Working var
        List<XslTransformETY> objects;
        // Create query
        Query q = Query.query(Criteria.where(Constants.App.DELETED).ne(true));
        try {
            // Execute
            objects = mongoTemplate.find(q, XslTransformETY.class);
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException("Unable to retrieve every available extension with their documents", e);
        }
        return objects;
    }

	public XslTransformETY findByTemplateIdRoot(String templateIdRoot) throws OperationException {
		try {
            Query query = new Query();
            query.addCriteria(Criteria.where(Constants.App.TEMPLATE_ID_ROOT).is(templateIdRoot).and(Constants.App.DELETED).ne(true));
            query.with(Sort.by(Direction.DESC, Constants.App.INSERTION_DATE));

            return mongoTemplate.findOne(query, XslTransformETY.class);
		} catch (MongoException e) {
			log.error(Constants.Logs.ERROR_FIND_TRANSFORM, e);
			throw new OperationException(Constants.Logs.ERROR_FIND_TRANSFORM, e);
		} catch (Exception ex) {
			log.error(Constants.Logs.ERROR_UPDATING_TRANSFORM + getClass(), ex);
			throw new BusinessException(Constants.Logs.ERROR_UPDATING_TRANSFORM + getClass(), ex);
		}
	}
	
	
}
