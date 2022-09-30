package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions;

public class ObjectNameNotValidException extends Exception{

    /**
     * Message constructor.
     *
     * @param msg	Message to be shown.
     */
    public ObjectNameNotValidException(final String msg) {
        super(msg);
    }
}