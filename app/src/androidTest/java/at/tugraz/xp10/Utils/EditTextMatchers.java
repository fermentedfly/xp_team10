/**
 * Used from following github repository:
 * https://gist.github.com/JehandadK/00af4ca95b5e3f215cbd
 */

package at.tugraz.xp10.Utils;

import android.content.res.Resources;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public final class EditTextMatchers {

    private EditTextMatchers() {
        // do not instantiate
    }

    public static Matcher<View> withErrorText(final String text) {
        return withErrorText(Matchers.is(text));
    }

    /**
     * Returns a matcher that matches {@link android.widget.EditText}s based on text property value.
     *
     * @param stringMatcher {@link Matcher} of {@link String} with text to match
     */
    public static Matcher<View> withErrorText(final Matcher<String> stringMatcher) {

        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with error text: ");
                stringMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(EditText view) {
                if (view.getError() == null) return stringMatcher.matches(view.getError());
                return stringMatcher.matches(view.getError().toString());
            }
        };
    }

    /**
     * Returns a matcher that matches a descendant of {@link android.widget.EditText} that is displaying the error
     * string associated with the given resource id.
     *
     * @param resourceId the string resource the text view is expected to hold.
     */
    public static Matcher<View> withErrorText(final int resourceId) {

        return new BoundedMatcher<View, EditText>(EditText.class) {
            private String resourceName = null;
            private String expectedText = null;

            @Override
            public void describeTo(Description description) {
                description.appendText("with error text from resource id: ");
                description.appendValue(resourceId);
                if (null != resourceName) {
                    description.appendText("[");
                    description.appendText(resourceName);
                    description.appendText("]");
                }
                if (null != expectedText) {
                    description.appendText(" value: ");
                    description.appendText(expectedText);
                }
            }

            @Override
            public boolean matchesSafely(EditText editText) {
                if (null == expectedText) {
                    try {
                        expectedText = editText.getResources().getString(resourceId);
                        resourceName = editText.getResources().getResourceEntryName(resourceId);
                    } catch (Resources.NotFoundException ignored) {
                        // view could be from a context unaware of the resource id
                    }
                }
                return null != expectedText && expectedText.equals(editText.getError());
            }
        };
    }
}
