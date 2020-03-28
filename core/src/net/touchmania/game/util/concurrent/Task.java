/*
 * Copyright 2020 Vincenzo Fortunato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.touchmania.game.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public abstract class Task<V> extends FutureTask<V> {
    public Task() {
        this(new CallableAdapter<V>());
    }
    
    private Task(CallableAdapter<V> adapter) {
        super(adapter);
        adapter.task = this;
    }
    
    protected abstract V call() throws Exception;
    
    private static final class CallableAdapter<V> implements Callable<V> {
        private Task<V> task;
        
        @Override
        public V call() throws Exception {
            return task.call();
        }
    }
}


