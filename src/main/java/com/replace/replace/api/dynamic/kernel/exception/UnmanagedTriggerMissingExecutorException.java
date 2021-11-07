package com.replace.replace.api.dynamic.kernel.exception;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class UnmanagedTriggerMissingExecutorException extends Throwable {

    public UnmanagedTriggerMissingExecutorException() {
        super( "@Unmanaged trigger must have executor" );
    }
}
