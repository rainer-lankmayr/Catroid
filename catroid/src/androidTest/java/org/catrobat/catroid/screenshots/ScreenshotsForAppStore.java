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

package org.catrobat.catroid.screenshots;

import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.web.assertion.WebViewAssertions.webMatches;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.web.assertion.WebAssertion;
import android.support.test.espresso.web.assertion.WebViewAssertions;
import android.support.test.espresso.web.matcher.DomMatchers;
import android.support.test.espresso.web.sugar.Web;
import android.support.test.espresso.web.webdriver.Locator;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;

import org.catrobat.catroid.CatroidApplication;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.ChangeSizeByNBrick;
import org.catrobat.catroid.content.bricks.IfThenLogicBeginBrick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.FormulaElement;
import org.catrobat.catroid.formulaeditor.FormulaElement.ElementType;
import org.catrobat.catroid.formulaeditor.Functions;
import org.catrobat.catroid.formulaeditor.InternFormulaParser;
import org.catrobat.catroid.formulaeditor.InternToken;
import org.catrobat.catroid.formulaeditor.InternTokenType;
import org.catrobat.catroid.formulaeditor.Operators;
import org.catrobat.catroid.R;
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

//import tools.fastlane.screengrab.FalconScreenshotStrategy;
import java.util.Locale;

import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy;
import tools.fastlane.screengrab.locale.LocaleTestRule;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static android.support.test.espresso.Espresso.;
import static android.support.test.espresso.web.assertion.WebViewAssertions.webMatches;
import static android.support.test.espresso.web.model.Atoms.getTitle;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.getText;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webClick;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webKeys;
import static org.catrobat.catroid.uiespresso.formulaeditor.utils.FormulaEditorDataListWrapper.onDataList;
import static org.catrobat.catroid.uiespresso.formulaeditor.utils.FormulaEditorWrapper.onFormulaEditor;
import static org.hamcrest.CoreMatchers.containsString;
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
//	public ActivityTestRule<MainMenuActivity> mActivityTestRule = new ActivityTestRule<>(MainMenuActivity.class);
//	public ActivityTestRule<MainMenuActivity> mActivityTestRule = new ActivityTestRule<>(MainMenuActivity.class, false, false);
	public ActivityTestRule<MainMenuActivity> mActivityTestRule =
			new ActivityTestRule<MainMenuActivity>(MainMenuActivity.class) {
				@Override
				protected void beforeActivityLaunched() {
					PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext())
							.edit()
							.putBoolean(AGREED_TO_PRIVACY_POLICY_SETTINGS_KEY, true)
							.commit();
				}
			};

	@Rule
	public BaseActivityInstrumentationRule<SpriteActivity> mFormulaEditorTestRule =
			new BaseActivityInstrumentationRule<>(SpriteActivity.class, SpriteActivity.EXTRA_FRAGMENT_POSITION,
			SpriteActivity.FRAGMENT_SCRIPTS);

	@Rule
	public ActivityTestRule<WebViewActivity> mWebActivityTestRule =
			new ActivityTestRule<WebViewActivity>(WebViewActivity.class,
					false, false) {
				@Override
				protected void afterActivityLaunched() {
					onWebView().forceJavascriptEnabled();
				}
			};

	@Before
	public void setUp() {
		Screengrab.setDefaultScreenshotStrategy(new UiAutomatorScreenshotStrategy());
//		Screengrab.setDefaultScreenshotStrategy(new FalconScreenshotStrategy(mActivityTestRule.getActivity()));

//		mActivityTestRule.launchActivity(null);
		sleep(1000);
	}

	@Test
	public void createScreenshotsApp() {
		onView(withText(R.string.main_menu_programs)).check(matches(isDisplayed()));

		takeScreenshot("screenshot1");

		onView(withText(R.string.main_menu_programs)).perform(click());

		ViewInteraction recyclerView2 = onView(
				allOf(withId(R.id.recycler_view),
						childAtPosition(
								withClassName(is("android.widget.FrameLayout")),
								2)));
		recyclerView2.perform(actionOnItemAtPosition(0, click()));

		ViewInteraction recyclerView3 = onView(
				allOf(withId(R.id.recycler_view),
						childAtPosition(
								withClassName(is("android.widget.FrameLayout")),
								2)));
		recyclerView3.perform(actionOnItemAtPosition(3, click()));

		onView(withText(R.string.scripts)).perform(click());
		onView(withId(R.id.button_play)).check(matches(isDisplayed()));
		takeScreenshot("screenshot2");

//		onView(withId(R.id.brick_if_begin_edit_text)).perform(click());
//		onView(withId(R.id.brick_set_variable_edit_text)).perform(click());
//		onView(withId(R.id.brick_wait_edit_text)).perform(click());
//		onView(withId(R.id.formula_editor_edit_field)).perform(click());

		DataInteraction linearLayout = onData(anything())
				.inAdapterView(allOf(withId(android.R.id.list),
						childAtPosition(
								withId(R.id.fragment_script),
								0)))
				.atPosition(7);
		linearLayout.perform(click());


		DataInteraction appCompatTextView = onData(anything())
				.inAdapterView(allOf(withId(R.id.select_dialog_listview),
						childAtPosition(
								withId(R.id.contentPanel),
								0)))
				.atPosition(3);
		appCompatTextView.perform(click());

		onView(withId(R.id.formula_editor_edit_field)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_ok)).check(matches(isDisplayed()));
		takeScreenshot("screenshot3");
	}

//	@Test
//	public void createScreenshotsFormulaEditor() {
//		Script script = BrickTestUtils.createProjectAndGetStartScript("FormulaEditor");
//		Formula formula = new Formula("x < 10");
//		script.addBrick(0, new IfThenLogicBeginBrick(formula));
//
//		mFormulaEditorTestRule.launchActivity();
//		onView(withId(R.id.brick_if_begin_edit_text)).perform(click());
//		onView(withId(R.id.formula_editor_edit_field)).perform(click());
//
//
////		onView(withId(R.id.brick_edit)).perform(click());
////		Brick.BrickField.IF_CONDITION;
//
////		onFormulaEditor()
////				.performOpenDataFragment();
////		final String varNameX = "x";
////		onDataList()
////				.performAdd(varNameX);
////
////		pressBack();
//
////		onView(withId(R.id.formula_editor_keyboard_data)).perform(click());
////		onFormulaEditor().performEnterFormula("(1)+1-1*1/1=1");
//
//		sleep(1000);
//		takeScreenshot("screenshot10");
//	}

	@Test
	public void createScreenshotsExplore() {
		onView(withText(R.string.main_menu_web)).perform(click());
		sleep(7000);

//		ViewInteraction webView = onView(withId((R.id.webView)));

//		onWebView().check(WebViewAssertions.webContent(DomMatchers.hasElementWithId("mostDownloaded")));
//		onWebView().check(WebViewAssertions.webContent(DomMatchers.containingTextInBody("Catrobat")));
//		onWebView().check(webMatches(getTitle(), is("Pocket Code Website")));  // works but not waiting

		onWebView()
				.withElement(findElement(Locator.ID, "mostDownloaded"))
				.perform(webClick());

		sleep(1000);

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

	private static Matcher<View> childAtPosition(
			final Matcher<View> parentMatcher, final int position) {

		return new TypeSafeMatcher<View>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("Child at position " + position + " in parent ");
				parentMatcher.describeTo(description);
			}

			@Override
			public boolean matchesSafely(View view) {
				ViewParent parent = view.getParent();
				return parent instanceof ViewGroup && parentMatcher.matches(parent)
						&& view.equals(((ViewGroup) parent).getChildAt(position));
			}
		};
	}
}
