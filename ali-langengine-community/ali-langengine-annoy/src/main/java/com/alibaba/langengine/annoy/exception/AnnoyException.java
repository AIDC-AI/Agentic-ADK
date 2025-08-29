/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.langengine.annoy.exception;


public class AnnoyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 构造函数
     */
    public AnnoyException() {
        super();
    }

    /**
     * 构造函数
     */
    public AnnoyException(String message) {
        super(message);
    }

    /**
     * 构造函数
     */
    public AnnoyException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     */
    public AnnoyException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数
     */
    public AnnoyException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 构造函数
     */
    public AnnoyException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * 获取错误代码
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置错误代码
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 索引构建异常
     */
    public static class IndexBuildException extends AnnoyException {
        public IndexBuildException(String message) {
            super("INDEX_BUILD_ERROR", message);
        }

        public IndexBuildException(String message, Throwable cause) {
            super("INDEX_BUILD_ERROR", message, cause);
        }
    }

    /**
     * 索引加载异常
     */
    public static class IndexLoadException extends AnnoyException {
        public IndexLoadException(String message) {
            super("INDEX_LOAD_ERROR", message);
        }

        public IndexLoadException(String message, Throwable cause) {
            super("INDEX_LOAD_ERROR", message, cause);
        }
    }

    /**
     * 向量搜索异常
     */
    public static class SearchException extends AnnoyException {
        public SearchException(String message) {
            super("SEARCH_ERROR", message);
        }

        public SearchException(String message, Throwable cause) {
            super("SEARCH_ERROR", message, cause);
        }
    }

    /**
     * 向量添加异常
     */
    public static class VectorAddException extends AnnoyException {
        public VectorAddException(String message) {
            super("VECTOR_ADD_ERROR", message);
        }

        public VectorAddException(String message, Throwable cause) {
            super("VECTOR_ADD_ERROR", message, cause);
        }
    }

    /**
     * 参数验证异常
     */
    public static class ParameterValidationException extends AnnoyException {
        public ParameterValidationException(String message) {
            super("PARAMETER_VALIDATION_ERROR", message);
        }

        public ParameterValidationException(String message, Throwable cause) {
            super("PARAMETER_VALIDATION_ERROR", message, cause);
        }
    }

    /**
     * 文件操作异常
     */
    public static class FileOperationException extends AnnoyException {
        public FileOperationException(String message) {
            super("FILE_OPERATION_ERROR", message);
        }

        public FileOperationException(String message, Throwable cause) {
            super("FILE_OPERATION_ERROR", message, cause);
        }
    }

    /**
     * 本地库加载异常
     */
    public static class NativeLibraryException extends AnnoyException {
        public NativeLibraryException(String message) {
            super("NATIVE_LIBRARY_ERROR", message);
        }

        public NativeLibraryException(String message, Throwable cause) {
            super("NATIVE_LIBRARY_ERROR", message, cause);
        }
    }
}
