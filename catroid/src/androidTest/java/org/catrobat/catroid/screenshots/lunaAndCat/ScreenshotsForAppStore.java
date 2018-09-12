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

package org.catrobat.catroid.screenshots.lunaAndCat;

import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.web.webdriver.Locator;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.bricks.IfThenLogicBeginBrick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.SpriteActivity;
import org.catrobat.catroid.ui.WebViewActivity;
import org.catrobat.catroid.uiespresso.content.brick.utils.BrickTestUtils;
import org.catrobat.catroid.uiespresso.util.rules.BaseActivityInstrumentationRule;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy;
import tools.fastlane.screengrab.locale.LocaleTestRule;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webClick;
import static org.catrobat.catroid.uiespresso.content.brick.utils.BrickDataInteractionWrapper.onBrickAtPosition;
import static org.catrobat.catroid.uiespresso.formulaeditor.utils.FormulaEditorWrapper.FORMULA_EDITOR_TEXT_FIELD_MATCHER;
import static org.catrobat.catroid.uiespresso.formulaeditor.utils.FormulaEditorWrapper.onFormulaEditor;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ScreenshotsForAppStore {

	private static final String AGREED_TO_PRIVACY_POLICY_SETTINGS_KEY = "AgreedToPrivacyPolicy";

	@ClassRule
	public static final LocaleTestRule localeTestRule = new LocaleTestRule();

	@Rule
	public ActivityTestRule<MainMenuActivity> mainActivityTestRule =
			new ActivityTestRule<>(MainMenuActivity.class, false, false);

	@Rule
	public BaseActivityInstrumentationRule<SpriteActivity> formulaEditorTestRule =
			new BaseActivityInstrumentationRule<>(SpriteActivity.class, SpriteActivity.EXTRA_FRAGMENT_POSITION,
			SpriteActivity.FRAGMENT_SCRIPTS);
	// safe
	// public BaseActivityInstrumentationRule(Class<T> activityClass, String extraFragementPosition, int fragment)

	@Rule
	public ActivityTestRule<WebViewActivity> webActivityTestRule =
			new ActivityTestRule<WebViewActivity>(WebViewActivity.class,
					false, false) {
				@Override
				protected void afterActivityLaunched() {
					onWebView().forceJavascriptEnabled();
				}
			};

	@Before
	public void setUp() {
		PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext())
							.edit()
							.putBoolean(AGREED_TO_PRIVACY_POLICY_SETTINGS_KEY, true)
							.commit();
		Screengrab.setDefaultScreenshotStrategy(new UiAutomatorScreenshotStrategy());
		sleep(1000);
	}

	@Test
	public void createScreenshotsApp() {
		mainActivityTestRule.launchActivity(null);

		onView(withText(R.string.main_menu_programs)).check(matches(isDisplayed()));
		takeScreenshot("screenshot1");

		onView(withText(R.string.main_menu_programs)).perform(click());
		onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
		onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
		onView(withText(R.string.scripts)).perform(click());
		onView(withId(R.id.button_play)).check(matches(isDisplayed()));
		takeScreenshot("screenshot2");

		onBrickAtPosition(7).onFormulaTextField(R.id.brick_wait_edit_text).perform(click());
		onFormulaEditor().performClickOn(FORMULA_EDITOR_TEXT_FIELD_MATCHER);
		onView(withId(R.id.formula_editor_edit_field)).perform(click());
		takeScreenshot("screenshot3");
	}

	@Test
	public void createScreenshotsWeb() {
		mainActivityTestRule.launchActivity(null);

		onView(withText(R.string.main_menu_web)).perform(click());
		sleep(7000);
		//onWebView().withElement(findElement(Locator.ID, "mostDownloaded")).perform(webClick());
		takeScreenshot("screenshot4");
	}

	private void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void takeScreenshot(String screenshotName) {
		if (screenshotName.equals("")) {
			screenshotName = "untitled";
		}
		Screengrab.screenshot(screenshotName);
	}
}
