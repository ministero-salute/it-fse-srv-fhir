package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions;


public class InvalidVersionException extends Exception {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 6134857493429760036L;

    /**
     * Message constructor.
     *
     * @param msg	Message to be shown.
     */
    public InvalidVersionException(final String msg) {
        super(msg);
    }

}
