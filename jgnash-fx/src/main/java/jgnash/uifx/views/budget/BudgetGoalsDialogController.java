/*
 * jGnash, a personal finance application
 * Copyright (C) 2001-2015 Craig Cavanaugh
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jgnash.uifx.views.budget;

import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import jgnash.engine.Account;
import jgnash.engine.CurrencyNode;
import jgnash.engine.budget.BudgetGoal;
import jgnash.engine.budget.BudgetPeriod;
import jgnash.engine.budget.BudgetPeriodDescriptor;
import jgnash.engine.budget.BudgetPeriodDescriptorFactory;
import jgnash.engine.budget.Pattern;
import jgnash.uifx.control.DecimalTextField;

/**
 * @author Craig Cavanaugh
 */
public class BudgetGoalsDialogController {

    @FXML
    private DecimalTextField amountDecimalTextField;

    @FXML
    private Spinner<Integer> endRowSpinner;

    @FXML
    private Spinner<Integer> startRowSpinner;

    @FXML
    private ComboBox<Pattern> patternComboBox;

    @FXML
    private DecimalTextField fillAllDecimalTextField;

    @FXML
    private TableView goalTable;

    @FXML
    private Label currencyLabel;

    @FXML
    private ComboBox<BudgetPeriod> periodComboBox;

    @FXML
    private ResourceBundle resources;

    private SimpleObjectProperty<Account> accountProperty = new SimpleObjectProperty<>();

    private SimpleObjectProperty<BudgetGoal> budgetGoalProperty = new SimpleObjectProperty<>();

    private IntegerProperty workingYearProperty = new SimpleIntegerProperty();

    private IntegerProperty descriptorSizeProperty = new SimpleIntegerProperty();

    @FXML
    private void initialize() {
        endRowSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1));
        startRowSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1));

        periodComboBox.getItems().addAll(BudgetPeriod.values());

        patternComboBox.getItems().addAll(Pattern.values());
        patternComboBox.setValue(Pattern.EveryRow);

        periodComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                budgetGoalProperty().get().setBudgetPeriod(newValue);
                descriptorSizeProperty.setValue(getDescriptors().size());
            }
        });

        budgetGoalProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                periodComboBox.setValue(newValue.getBudgetPeriod());
            }
        });

        // the spinner factory max values do not like being bound; Set value instead
        descriptorSizeProperty.addListener((observable, oldValue, newValue) -> {
            ((SpinnerValueFactory.IntegerSpinnerValueFactory)endRowSpinner.getValueFactory()).setMax(newValue.intValue());
            ((SpinnerValueFactory.IntegerSpinnerValueFactory)startRowSpinner.getValueFactory()).setMax(newValue.intValue());
            endRowSpinner.getValueFactory().setValue(newValue.intValue());
        });

        // account has changed; update currency related properties
        accountProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final CurrencyNode currencyNode = newValue.getCurrencyNode();

                currencyLabel.setText(currencyNode.getSymbol());
                fillAllDecimalTextField.scaleProperty().setValue(currencyNode.getScale());
                amountDecimalTextField.scaleProperty().setValue(currencyNode.getScale());
            }
        });

    }

    public SimpleObjectProperty<Account> accountProperty() {
        return accountProperty;
    }

    /**
     * A working clone should be set instead of the base.  This property will be modified as needed.
     *
     * @return BudgetGoal property
     */
    public SimpleObjectProperty<BudgetGoal> budgetGoalProperty() {
        return budgetGoalProperty;
    }

    public IntegerProperty workingYearProperty() {
        return workingYearProperty;
    }

    private List<BudgetPeriodDescriptor> getDescriptors() {
        return  BudgetPeriodDescriptorFactory.getDescriptors(workingYearProperty.get(), budgetGoalProperty.get().getBudgetPeriod());
    }

    @FXML
    private void handleOkayAction() {
        ((Stage) periodComboBox.getScene().getWindow()).close();
    }

    @FXML
    private void handleCloseAction() {
        ((Stage) periodComboBox.getScene().getWindow()).close();
    }

    @FXML
    private void handleHistoricalFill() {

    }

    @FXML
    private void handleFillAllAction() {
    }

    @FXML
    private void handlePatternFillAction() {
    }
}
