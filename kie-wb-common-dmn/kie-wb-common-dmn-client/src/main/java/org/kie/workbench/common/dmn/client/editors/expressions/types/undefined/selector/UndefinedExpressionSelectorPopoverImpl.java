/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.dmn.client.editors.expressions.types.undefined.selector;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.common.client.dom.HTMLElement;
import org.kie.workbench.common.dmn.api.qualifiers.DMNEditor;
import org.kie.workbench.common.dmn.client.editors.expressions.types.ExpressionEditorDefinition;
import org.kie.workbench.common.dmn.client.editors.expressions.types.ExpressionEditorDefinitions;
import org.kie.workbench.common.dmn.client.editors.expressions.types.undefined.UndefinedExpressionGrid;

@ApplicationScoped
public class UndefinedExpressionSelectorPopoverImpl implements UndefinedExpressionSelectorPopoverView.Presenter {

    private UndefinedExpressionSelectorPopoverView view;

    private Optional<UndefinedExpressionGrid> binding = Optional.empty();

    public UndefinedExpressionSelectorPopoverImpl() {
        //CDI proxy
    }

    @Inject
    public UndefinedExpressionSelectorPopoverImpl(final UndefinedExpressionSelectorPopoverView view,
                                                  final @DMNEditor Supplier<ExpressionEditorDefinitions> expressionEditorDefinitionsSupplier) {
        this.view = view;

        view.init(this);
        view.setExpressionEditorDefinitions(expressionEditorDefinitionsSupplier
                                                    .get()
                                                    .stream()
                                                    .filter(definition -> definition.getModelClass().isPresent())
                                                    .collect(Collectors.toList()));
    }

    @Override
    public void onExpressionEditorDefinitionSelected(final ExpressionEditorDefinition definition) {
        binding.ifPresent(b -> {
            b.onExpressionTypeChanged(definition.getType());
            view.hide();
        });
    }

    @Override
    public HTMLElement getElement() {
        return view.getElement();
    }

    @Override
    public void bind(final UndefinedExpressionGrid bound,
                     final int uiRowIndex,
                     final int uiColumnIndex) {
        binding = Optional.ofNullable(bound);
    }

    @Override
    public void show(final Optional<String> editorTitle) {
        binding.ifPresent(b -> view.show(editorTitle));
    }

    @Override
    public void hide() {
        binding.ifPresent(b -> view.hide());
    }
}
