/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.names;

/**
 * Name parse exception.
 */
public final class NameParseException extends RuntimeException {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -2375904385006224156L;

    /**
     * Constructor.
     */
    public NameParseException() {
        super();
    }

    /**
     * Contructor with message.
     *
     * @param message message
     */
    public NameParseException(String message) {
        super(message);
    }

    /**
     * Constructor with case.
     *
     * @param cause cause
     */
    public NameParseException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor with message and cause.
     *
     * @param message message
     * @param cause cause
     */
    public NameParseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Complete constructor.
     *
     * @param message message
     * @param cause cause
     * @param enableSuppression flag to enable suppression
     * @param writableStackTrace a writable stack trace
     */
    public NameParseException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
