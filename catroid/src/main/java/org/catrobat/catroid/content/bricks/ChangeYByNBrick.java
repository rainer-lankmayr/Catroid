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

public class ChangeYByNBrick extends FormulaBrick {

	private static final long serialVersionUID = 1L;

	public ChangeYByNBrick() {
		addAllowedBrickField(BrickField.Y_POSITION_CHANGE);
	}

	public ChangeYByNBrick(int yMovementValue) {
		initializeBrickFields(new Formula(yMovementValue));
	}

	public ChangeYByNBrick(Formula yMovement) {
		initializeBrickFields(yMovement);
	}

	private void initializeBrickFields(Formula yMovement) {
		addAllowedBrickField(BrickField.Y_POSITION_CHANGE);
		setFormulaWithBrickField(BrickField.Y_POSITION_CHANGE, yMovement);
	}

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.Y_POSITION_CHANGE).getRequiredResources();
	}

	@Override
	public int getViewResource() {
		return R.layout.brick_change_y;
	}

	@Override
	public View getView(Context context) {
		super.getView(context);
		TextView editY = view.findViewById(R.id.brick_change_y_edit_text);
		getFormulaWithBrickField(BrickField.Y_POSITION_CHANGE).setTextFieldId(R.id.brick_change_y_edit_text);
		getFormulaWithBrickField(BrickField.Y_POSITION_CHANGE).refreshTextField(view);
		editY.setOnClickListener(this);
		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		View prototypeView = super.getPrototypeView(context);
		TextView textYMovement = prototypeView.findViewById(R.id.brick_change_y_edit_text);
		textYMovement.setText(formatNumberForPrototypeView(BrickValues.CHANGE_Y_BY));
		return prototypeView;
	}

	@Override
	public List<ScriptSequenceAction> addActionToSequence(Sprite sprite, ScriptSequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory()
				.createChangeYByNAction(sprite, getFormulaWithBrickField(BrickField.Y_POSITION_CHANGE)));
		return null;
	}

	@Override
	public void showFormulaEditorToEditFormula(View view) {
		FormulaEditorFragment.showFragment(view, this, BrickField.Y_POSITION_CHANGE);
	}
}
