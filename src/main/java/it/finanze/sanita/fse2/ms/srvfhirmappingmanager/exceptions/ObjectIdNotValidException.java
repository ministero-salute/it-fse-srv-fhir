package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions;

public class ObjectIdNotValidException extends Exception{

    /**
     * Message constructor.
     *
     * @param msg	Message to be shown.
     */
    public ObjectIdNotValidException(final String msg) {
        super(msg);
    }
}