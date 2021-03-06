/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ScriptSequenceAction;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

import java.util.List;

public class TurnRightBrick extends FormulaBrick {

	private static final long serialVersionUID = 1L;
	private transient View prototypeView;

	public TurnRightBrick() {
		addAllowedBrickField(BrickField.TURN_RIGHT_DEGREES);
	}

	public TurnRightBrick(double degreesValue) {
		initializeBrickFields(new Formula(degreesValue));
	}

	public TurnRightBrick(Formula degreesFormula) {
		initializeBrickFields(degreesFormula);
	}

	private void initializeBrickFields(Formula degrees) {
		addAllowedBrickField(BrickField.TURN_RIGHT_DEGREES);
		setFormulaWithBrickField(BrickField.TURN_RIGHT_DEGREES, degrees);
	}

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.TURN_RIGHT_DEGREES).getRequiredResources();
	}

	@Override
	public int getViewResource() {
		return R.layout.brick_turn_right;
	}

	@Override
	public View getView(Context context) {
		super.getView(context);
		TextView editDegrees = (TextView) view.findViewById(R.id.brick_turn_right_edit_text);
		getFormulaWithBrickField(BrickField.TURN_RIGHT_DEGREES).setTextFieldId(R.id.brick_turn_right_edit_text);
		getFormulaWithBrickField(BrickField.TURN_RIGHT_DEGREES).refreshTextField(view);

		editDegrees.setOnClickListener(this);
		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = super.getPrototypeView(context);
		TextView textDegrees = (TextView) prototypeView.findViewById(R.id.brick_turn_right_edit_text);
		textDegrees.setText(formatNumberForPrototypeView(BrickValues.TURN_DEGREES));
		return prototypeView;
	}

	@Override
	public List<ScriptSequenceAction> addActionToSequence(Sprite sprite, ScriptSequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createTurnRightAction(sprite,
				getFormulaWithBrickField(BrickField.TURN_RIGHT_DEGREES)));
		return null;
	}

	@Override
	public void showFormulaEditorToEditFormula(View view) {
		FormulaEditorFragment.showFragment(view, this, BrickField.TURN_RIGHT_DEGREES);
	}
}
