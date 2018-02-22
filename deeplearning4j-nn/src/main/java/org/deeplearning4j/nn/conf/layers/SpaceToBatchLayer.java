/*-
 *
 *  * Copyright 2017 Skymind,Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */
package org.deeplearning4j.nn.conf.layers;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.deeplearning4j.nn.api.ParamInitializer;
import org.deeplearning4j.nn.conf.InputPreProcessor;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.params.EmptyParamInitializer;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Space to batch utility layer for convolutional input types.
 *
 * N-dimensional space to batch operation. Transforms data from a tensor from M spatial dimensions into batch dimension
 * according to the "blocks" specified (a vector of length M). Afterwards the spatial dimensions are optionally padded,
 * as specified in "padding", a tensor of dim (M, 2), denoting the padding range.
 * <p>
 * Example:
 * input:         [[[[1], [2]], [[3], [4]]]]
 * input shape:   [1, 2, 2, 1]
 * blocks:        [2, 2]
 * padding:       [[0, 0], [0, 0]]
 * <p>
 * output:        [[[[1]]], [[[2]]], [[[3]]], [[[4]]]]
 * output shape:  [4, 1, 1, 1]
 *
 * @author Max Pumperla
 */

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class SpaceToBatchLayer extends Layer {


    protected int[] blocks;
    protected int[][] padding;


    protected SpaceToBatchLayer(SpaceToBatchBuilder builder) {
        super(builder);
        this.blocks = builder.blocks;
    }

    @Override
    public SpaceToBatchLayer clone() {
        SpaceToBatchLayer clone = (SpaceToBatchLayer) super.clone();
        return clone;
    }

    @Override
    public ParamInitializer initializer() {
        return EmptyParamInitializer.getInstance();
    }


    @Override
    public void setNIn(InputType inputType, boolean override) {
        //No op: space to batch layer doesn't have nIn value
    }

    @Override
    public InputPreProcessor getPreProcessorForInputType(InputType inputType) {
        if (inputType == null) {
            throw new IllegalStateException("Invalid input for space to batch layer (layer name=\"" + getLayerName()
                            + "\"): input is null");
        }
        return InputTypeUtil.getPreProcessorForInputTypeCnnLayers(inputType, getLayerName());
    }

    @Override
    public double getL1ByParam(String paramName) {
        //Not applicable
        return 0;
    }

    @Override
    public double getL2ByParam(String paramName) {
        //Not applicable
        return 0;
    }

    @Override
    public boolean isPretrainParam(String paramName) {
        throw new UnsupportedOperationException("SpaceToBatchLayer does not contain parameters");
    }


    @NoArgsConstructor
    protected static abstract class SpaceToBatchBuilder<T extends SpaceToBatchBuilder<T>>
                    extends Builder<T> {
        protected int[] blocks;
        protected int[][] padding;

        protected SpaceToBatchBuilder(int[] blocks) {

            this.blocks = blocks;
            int numBlocks = blocks.length;
            padding = new int[numBlocks][2];
            for (int i = 0; i < numBlocks; i++) {
                padding[i] = new int[] {0, 0};
            }
        }

        protected SpaceToBatchBuilder(int[] blocks, int[][] padding) {
            this.blocks = blocks;
            this.padding = padding;

        }
    }

}
