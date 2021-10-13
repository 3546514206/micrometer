/**
 * Copyright 2018 VMware, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.core.instrument.util;

/**
 * @deprecated this will be removed and should not be used
 */
@Deprecated
public final class JsonUtils {

    private JsonUtils() {
    }
    
    /**
     * @param unformattedJsonString JSON string that has not been formatted at all.
     * @return the input string unchanged
     * @deprecated this will be removed and no longer does anything meaningful
     */
    @Deprecated
    public static String prettyPrint(String unformattedJsonString) {
        return unformattedJsonString;
    }
}