/*
 * Copyright 2019-2025 CrowdOS Group, Northwestern Polytechnical University, Inc. <https://github.com/crowdosNWPU/CrowdOS>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * imitations under the License.
 */
package cn.crowdos.kernel;

import cn.crowdos.kernel.constraint.InvalidConstraintException;

/**
 * <p>Thrown by
 * <ul>
 *     <li>{@link Decomposer#decompose(int)}</li>
 * </ul>
 * <p>to indicate something exceptions occurred during the decomposition operation. The most
 * common cause is {@link InvalidConstraintException}.</p> //todo update doc
 *
 * @see InvalidConstraintException
 * @since 1.0.0
 * @author loyx
 */
public class DecomposeException extends Exception{


    private static final long serialVersionUID = -3846151496338932949L;

    /**
     * Constructs an InvalidConstraintException with the specified
     * cause. The most common cause is {@link InvalidConstraintException}
     *
     * @param cause the cause (which is saved for later retrieval by the
     *         {@link Throwable#getCause()} method).
     */
    public DecomposeException(Throwable cause) {
        super(cause==null ? null : cause.toString());
        this.initCause(cause);
    }

    /**
     * Constructs a new DecomposeException with the specified detail message and
     * cause.
     *
     * @param  message the detail message.
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A {@code null} value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public DecomposeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecomposeException(String message) {
        super(message);
    }
}
