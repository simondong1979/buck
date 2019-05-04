/*
 * Copyright 2019-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.facebook.buck.core.graph.transformation.composition;

import com.facebook.buck.core.graph.transformation.ComputationEnvironment;
import com.facebook.buck.core.graph.transformation.GraphComputation;
import com.facebook.buck.core.graph.transformation.model.ComposedComputationIdentifier;
import com.facebook.buck.core.graph.transformation.model.ComposedKey;
import com.facebook.buck.core.graph.transformation.model.ComposedResult;
import com.facebook.buck.core.graph.transformation.model.ComputeKey;
import com.facebook.buck.core.graph.transformation.model.ComputeResult;
import com.google.common.collect.ImmutableSet;

/**
 * A {@link GraphComputation} that represents the {@link
 * com.facebook.buck.core.graph.transformation.composition.Composition} of a {@link
 * ComposedComputation} that we can the base computation, a {@link Composer}, and a {@link
 * Transformer}.
 *
 * <p>This computation has a {@link ComposedKey} of the primary computation key and the result of
 * the {@link Transformer}. After the base computation completes, the {@link Composer} will be
 * invoked for each individual result in the {@link ComposedResult} of the base computation,
 * returning a set of dependencies necessary for the transform step. Then {@link Transformer} is
 * invoked with the dependencies.
 *
 * @param <Key1> the composed key type of the base computation
 * @param <Result1> the composed result type of the base computation
 * @param <Result2> the composed result type of this computation
 */
public interface ComposedComputation<
        Key1 extends ComputeKey<Result1>,
        Result1 extends ComputeResult,
        Result2 extends ComputeResult>
    extends GraphComputation<ComposedKey<Key1, Result2>, ComposedResult<Result2>> {

  @Override
  ComposedComputationIdentifier<Result2> getIdentifier();

  @Override
  ComposedResult<Result2> transform(ComposedKey<Key1, Result2> key, ComputationEnvironment env)
      throws Exception;

  @Override
  ImmutableSet<? extends ComputeKey<? extends ComputeResult>> discoverDeps(
      ComposedKey<Key1, Result2> key, ComputationEnvironment env) throws Exception;

  @Override
  ImmutableSet<? extends ComputeKey<? extends ComputeResult>> discoverPreliminaryDeps(
      ComposedKey<Key1, Result2> key);
}
