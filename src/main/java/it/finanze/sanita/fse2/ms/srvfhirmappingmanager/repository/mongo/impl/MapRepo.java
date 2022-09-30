package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.mongo.impl;

import com.mongodb.MongoException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IMapRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.MapETY;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Logs.ERROR_UNABLE_FIND_DELETIONS;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Logs.ERROR_UNABLE_FIND_INSERTIONS;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.MapETY.FIELD_ID;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.MapETY.FIELD_NAME;
import static org.springframework.data.mongodb.core.BulkOperations.BulkMode.UNORDERED;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;


@Repository
public class MapRepo implements IMapRepo {

    @Autowired
    private MongoTemplate mongo;

    @Override
    public boolean isDocumentInserted(String name) throws OperationException {
        // Init result variable
        boolean res;
        // Create query
        Query query = new Query();
        query.addCriteria(
            where(FIELD_NAME).is(name).and("deleted").is(false)
        );
        try {
            // Execute
            res = mongo.exists(query, MapETY.class);
        } catch(MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException("Unable to verify if given document is inserted" , e);
        }
        // Return data
        return res;
    }

    @Override
    public MapETY findDocByName(String name) throws OperationException {
        // Working var
        MapETY out;
        // Create query
        Query query = new Query();
        query.addCriteria(
            Criteria.where(FIELD_NAME).is(name).and("deleted").is(false)
        );
        try {
            // Execute
            out = mongo.findOne(query, MapETY.class);
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException("Unable to retrieve map by name", e);
        }
        return out;
    }

    @Override
    public MapETY insertDocByName(MapETY entity) throws OperationException {
        // Working var
        MapETY out;
        try {
            // Execute
            out = mongo.insert(entity);
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException("Unable to insert map by name", e);
        }
        return out;
    }

    @Override
    public MapETY updateDocByName(MapETY current, MapETY newest) throws OperationException {
        // Create bulk operation
        BulkOperations ops = mongo.bulkOps(UNORDERED, MapETY.class);
        // Create query to match the required file
        Query query = new Query();
        query.addCriteria(
            where(FIELD_ID).is(new ObjectId(current.getId()))
        );
        query.addCriteria(
            where("deleted").is(false)
        );
        // Set fields to modify
        Update update = new Update();
        update.set(FIELD_LAST_UPDATE, new Date());
        update.set("deleted", true);
        // Creating query to mark as deleted the old file
        ops.updateOne(query, update);
        // Creating query to insert the new ones
        ops.insert(newest);
        // Now, we reach the database instance with the queries
        try {
            // Execute
            ops.execute();
        }catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException("Unable to update map by name" , e);
        }
        return newest;
    }

    @Override
    public MapETY deleteDocByName(String name) throws OperationException {
        // Working var
        MapETY out;
        // Create query
        Query query = new Query();
        query.addCriteria(where(FIELD_NAME).is(name));
        query.addCriteria(where("deleted").is(false));
        // Create update definition
        Update update = new Update();
        update.set(FIELD_LAST_UPDATE, new Date());
        update.set("deleted", true);
        // Get doc
        out = findDocByName(name);
        try {
            // Execute
            mongo.updateFirst(query, update, MapETY.class);
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException("Unable to insert map by name", e);
        }
        // Return removed doc
        return out;
    }

    @Override
    public MapETY findDocById(String id) throws OperationException {
        // Working var
        MapETY out;
        // Create query
        Query query = new Query();
        query.addCriteria(
            Criteria.where(FIELD_ID).is(id).and("deleted").is(false)
        );
        try {
            // Execute
            out = mongo.findOne(query, MapETY.class);
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException("Unable to retrieve map by id", e);
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
    public List<MapETY> getInsertions(Date lastUpdate) throws OperationException {
        // Working var
        List<MapETY> objects;
        // Create query
        Query q = Query.query(
            Criteria.where(FIELD_INSERTION_DATE).gt(lastUpdate).and("deleted").ne(true)
        );
        try {
            // Execute
            objects = mongo.find(q, MapETY.class);
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
    public List<MapETY> getDeletions(Date lastUpdate) throws OperationException {
        // Working var
        List<MapETY> objects;
        // Create query
        Query q = Query.query(
            Criteria.where(FIELD_LAST_UPDATE).gt(lastUpdate)
                .and(FIELD_INSERTION_DATE).lte(lastUpdate)
                .and("deleted").is(true)
        );
        try {
            // Execute
            objects = mongo.find(q, MapETY.class);
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
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
    public List<MapETY> getEveryActiveDocument() throws OperationException {
        // Working var
        List<MapETY> objects;
        // Create query
        Query q = query(where("deleted").ne(true));
        try {
            // Execute
            objects = mongo.find(q, MapETY.class);
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException("Unable to retrieve every available valueset", e);
        }
        return objects;
    }
}
