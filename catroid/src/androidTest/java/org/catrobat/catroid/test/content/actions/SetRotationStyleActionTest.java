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
package org.catrobat.catroid.test.content.actions;

import android.support.test.runner.AndroidJUnit4;

import com.badlogic.gdx.scenes.scene2d.Action;

import org.catrobat.catroid.content.ActionFactory;
import org.catrobat.catroid.content.Look;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.PointInDirectionBrick.Direction;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.physics.PhysicsLook;
import org.catrobat.catroid.physics.PhysicsObject;
import org.catrobat.catroid.physics.PhysicsWorld;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SetRotationStyleActionTest {

	private Sprite sprite;
	private PhysicsWorld physicsWorld;

	@Before
	public void setUp() throws Exception {
		sprite = new Sprite("testSprite");
		physicsWorld = new PhysicsWorld(1920, 1600);
	}

	@Test
	public void testNormalMode() {
		ActionFactory factory = sprite.getActionFactory();
		Action rotationStyleAction = factory.createSetRotationStyleAction(sprite, Look.ROTATION_STYLE_ALL_AROUND);
		Action pointInDirectionAction = factory.createPointInDirectionAction(sprite, new Formula(Direction.RIGHT
				.getDegrees()));

		rotationStyleAction.act(1.0f);
		pointInDirectionAction.act(1.0f);
		assertEquals(90f, sprite.look.getDirectionInUserInterfaceDimensionUnit());
	}

	@Test
	public void testNoMode() {
		ActionFactory factory = sprite.getActionFactory();
		Action rotationStyleAction = factory.createSetRotationStyleAction(sprite, Look.ROTATION_STYLE_NONE);
		Action pointInDirectionAction = factory.createPointInDirectionAction(sprite, new Formula(Direction.LEFT
				.getDegrees()));

		rotationStyleAction.act(1.0f);
		pointInDirectionAction.act(1.0f);

		assertEquals(-90f, sprite.look.getDirectionInUserInterfaceDimensionUnit());
	}

	@Test
	public void testLRMode() {
		ActionFactory factory = sprite.getActionFactory();
		Action rotationStyleAction = factory.createSetRotationStyleAction(sprite, Look.ROTATION_STYLE_LEFT_RIGHT_ONLY);
		Action pointInDirectionAction = factory.createPointInDirectionAction(sprite, new Formula(Direction.LEFT
				.getDegrees()));

		rotationStyleAction.act(1.0f);
		pointInDirectionAction.act(1.0f);

		assertEquals(-90f, sprite.look.getDirectionInUserInterfaceDimensionUnit());
	}

	//Directions here get funky because in physics there is no UI Degree Offset as in the normal looks
	//Right is Left, Left is Right, Up is Up and Down is Down

	@Test
	public void testNormalModeInPhysics() {
		PhysicsObject physicsObject = physicsWorld.getPhysicsObject(sprite);
		PhysicsLook physicsLook = new PhysicsLook(sprite, physicsWorld);

		physicsLook.setRotationMode(Look.ROTATION_STYLE_ALL_AROUND);

		physicsLook.setRotation((float) Direction.RIGHT.getDegrees());
		assertEquals(90f, physicsObject.getDirection());
		assertEquals(90f, physicsLook.getRotation());

		physicsLook.setRotation((float) Direction.LEFT.getDegrees());
		assertEquals(-90f, physicsObject.getDirection());
		assertEquals(-90f, physicsLook.getRotation());

		physicsLook.setRotation((float) Direction.UP.getDegrees());
		assertEquals(0f, physicsObject.getDirection());
		assertEquals(0f, physicsLook.getRotation());

		physicsLook.setRotation((float) Direction.DOWN.getDegrees());
		assertEquals(180f, physicsObject.getDirection());
		assertEquals(180f, physicsLook.getRotation());
	}

	@Test
	public void testNoModeInPhysics() {
		PhysicsObject physicsObject = physicsWorld.getPhysicsObject(sprite);
		PhysicsLook physicsLook = new PhysicsLook(sprite, physicsWorld);

		physicsLook.setRotationMode(Look.ROTATION_STYLE_NONE);

		physicsLook.setRotation((float) Direction.RIGHT.getDegrees());
		assertEquals(90f, physicsObject.getDirection());
		assertEquals(0f, physicsLook.getRotation());

		physicsLook.setRotation((float) Direction.LEFT.getDegrees());
		assertEquals(-90f, physicsObject.getDirection());
		assertEquals(0f, physicsLook.getRotation());

		physicsLook.setRotation((float) Direction.UP.getDegrees());
		assertEquals(0f, physicsObject.getDirection());
		assertEquals(0f, physicsLook.getRotation());

		physicsLook.setRotation((float) Direction.DOWN.getDegrees());
		assertEquals(180f, physicsObject.getDirection());
		assertEquals(0f, physicsLook.getRotation());
	}

	@Test
	public void testLRModeInPhysics() {
		PhysicsObject physicsObject = physicsWorld.getPhysicsObject(sprite);
		PhysicsLook physicsLook = new PhysicsLook(sprite, physicsWorld);

		physicsLook.setRotationMode(Look.ROTATION_STYLE_LEFT_RIGHT_ONLY);

		physicsLook.setRotation((float) Direction.RIGHT.getDegrees());
		assertEquals(90f, physicsObject.getDirection());
		assertEquals(0f, physicsLook.getRotation());

		physicsLook.setRotation((float) Direction.LEFT.getDegrees());
		assertEquals(-90f, physicsObject.getDirection());
		assertEquals(0f, physicsLook.getRotation());

		physicsLook.setRotation((float) Direction.UP.getDegrees());
		assertEquals(0f, physicsObject.getDirection());
		assertEquals(0f, physicsLook.getRotation());

		physicsLook.setRotation((float) Direction.DOWN.getDegrees());
		assertEquals(180f, physicsObject.getDirection());
		assertEquals(0f, physicsLook.getRotation());
	}
}
