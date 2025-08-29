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
package com.alibaba.langengine.annoy.native_;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.alibaba.langengine.annoy.exception.AnnoyException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.alibaba.langengine.annoy.AnnoyConfiguration.ANNOY_NATIVE_LIBRARY_PATH;

/**
 * Annoy原生库的JNA接口
 * 提供与Annoy C++库的直接交互能力
 *
 * @author xiaoxuan.lp
 */
public interface AnnoyNativeLibrary extends Library {

    Logger log = LoggerFactory.getLogger(AnnoyNativeLibrary.class);

    /**
     * 单例实例
     */
    AnnoyNativeLibrary INSTANCE = loadLibrary();

    /**
     * 创建Annoy索引
     * 
     * @param dimension 向量维度
     * @param metric 距离度量类型 ("angular", "euclidean", "manhattan", "hamming", "dot")
     * @return 索引指针
     */
    Pointer annoy_create_index(int dimension, String metric);

    /**
     * 销毁Annoy索引
     * 
     * @param index 索引指针
     */
    void annoy_destroy_index(Pointer index);

    /**
     * 添加向量到索引
     * 
     * @param index 索引指针
     * @param item 向量ID
     * @param vector 向量数据
     * @return 是否成功
     */
    boolean annoy_add_item(Pointer index, int item, float[] vector);

    /**
     * 构建索引
     * 
     * @param index 索引指针
     * @param nTrees 树的数量
     * @return 是否成功
     */
    boolean annoy_build(Pointer index, int nTrees);

    /**
     * 保存索引到文件
     * 
     * @param index 索引指针
     * @param filename 文件名
     * @return 是否成功
     */
    boolean annoy_save(Pointer index, String filename);

    /**
     * 从文件加载索引
     * 
     * @param index 索引指针
     * @param filename 文件名
     * @return 是否成功
     */
    boolean annoy_load(Pointer index, String filename);

    /**
     * 卸载索引
     * 
     * @param index 索引指针
     */
    void annoy_unload(Pointer index);

    /**
     * 获取最近邻
     * 
     * @param index 索引指针
     * @param item 查询向量ID
     * @param n 返回结果数量
     * @param searchK 搜索参数
     * @param result 结果数组
     * @param distances 距离数组
     * @return 实际返回的结果数量
     */
    int annoy_get_nns_by_item(Pointer index, int item, int n, int searchK, 
                              int[] result, float[] distances);

    /**
     * 通过向量获取最近邻
     * 
     * @param index 索引指针
     * @param vector 查询向量
     * @param n 返回结果数量
     * @param searchK 搜索参数
     * @param result 结果数组
     * @param distances 距离数组
     * @return 实际返回的结果数量
     */
    int annoy_get_nns_by_vector(Pointer index, float[] vector, int n, int searchK,
                                int[] result, float[] distances);

    /**
     * 获取向量
     * 
     * @param index 索引指针
     * @param item 向量ID
     * @param vector 输出向量数组
     * @return 是否成功
     */
    boolean annoy_get_item(Pointer index, int item, float[] vector);

    /**
     * 获取两个向量之间的距离
     * 
     * @param index 索引指针
     * @param i 第一个向量ID
     * @param j 第二个向量ID
     * @return 距离值
     */
    float annoy_get_distance(Pointer index, int i, int j);

    /**
     * 获取索引中的向量数量
     * 
     * @param index 索引指针
     * @return 向量数量
     */
    int annoy_get_n_items(Pointer index);

    /**
     * 获取向量维度
     * 
     * @param index 索引指针
     * @return 向量维度
     */
    int annoy_get_dimension(Pointer index);

    /**
     * 检查索引是否已构建
     * 
     * @param index 索引指针
     * @return 是否已构建
     */
    boolean annoy_is_built(Pointer index);

    /**
     * 设置种子值
     * 
     * @param index 索引指针
     * @param seed 种子值
     */
    void annoy_set_seed(Pointer index, int seed);

    /**
     * 加载原生库
     */
    static AnnoyNativeLibrary loadLibrary() {
        try {
            String libraryPath = determineLibraryPath();
            log.info("Loading Annoy native library from: {}", libraryPath);

            if (StringUtils.isNotEmpty(libraryPath)) {
                return Native.load(libraryPath, AnnoyNativeLibrary.class);
            } else {
                // 尝试从系统路径加载
                return Native.load("annoy", AnnoyNativeLibrary.class);
            }
        } catch (UnsatisfiedLinkError e) {
            log.warn("Failed to load Annoy native library: {}", e.getMessage());
            // 在测试环境中，我们返回 null 而不是抛出异常
            return null;
        }
    }

    /**
     * 确定库路径
     */
    static String determineLibraryPath() {
        // 1. 检查配置的路径
        if (StringUtils.isNotEmpty(ANNOY_NATIVE_LIBRARY_PATH)) {
            return ANNOY_NATIVE_LIBRARY_PATH;
        }

        // 2. 检查系统属性
        String systemPath = System.getProperty("annoy.library.path");
        if (StringUtils.isNotEmpty(systemPath)) {
            return systemPath;
        }

        // 3. 检查环境变量
        String envPath = System.getenv("ANNOY_LIBRARY_PATH");
        if (StringUtils.isNotEmpty(envPath)) {
            return envPath;
        }

        // 4. 根据操作系统确定默认库名
        String osName = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        
        if (osName.contains("windows")) {
            return "annoy.dll";
        } else if (osName.contains("mac")) {
            return "libannoy.dylib";
        } else {
            return "libannoy.so";
        }
    }

    /**
     * 验证库是否可用
     */
    static boolean isLibraryAvailable() {
        try {
            // 首先尝试加载库
            AnnoyNativeLibrary library = INSTANCE;
            if (library == null) {
                return false;
            }

            // 尝试创建一个简单的索引来验证库是否正常工作
            Pointer testIndex = library.annoy_create_index(2, "euclidean");
            if (testIndex != null) {
                library.annoy_destroy_index(testIndex);
                return true;
            }
            return false;
        } catch (Throwable e) {
            log.debug("Annoy native library is not available: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取库版本信息（如果支持）
     */
    default String getLibraryVersion() {
        return "Unknown";
    }

    /**
     * 获取支持的距离度量类型
     */
    static String[] getSupportedMetrics() {
        return new String[]{"angular", "euclidean", "manhattan", "hamming", "dot"};
    }
}
