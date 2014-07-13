package edu.umd.rhsmith.diads.meater.modules.tweater.oauth;

import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;
import edu.umd.rhsmith.diads.meater.util.console.BooleanPrompter;

public class OAuthSetupOperation extends SetupConsoleOperation {

	private OAuthConfig owner;

	public OAuthSetupOperation(OAuthConfig owner) {
		super("Set up this OAuth configuration", "setup");
		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.getConsole()
				.say("This program will help you acquire the necessary credentials for using the\n"
						+ "Twitter Streaming API. There are two kinds of tokens needed: a Consumer Token\n"
						+ "and an Access Token. The former may already be present in the configuration file,\n"
						+ "but the latter requires that you log in to Twitter and authorize TwEater.\n"
						+ "This operation will walk you through the process.\n");

		TwitterStream tw = new TwitterStreamFactory().getInstance();
		loadConsumerToken(setup, tw);
		loadAccessToken(setup, tw);
	}

	private void loadConsumerToken(MEaterSetupConsole setup, TwitterStream tw) {
		boolean missing = owner.getConsumerKey().isEmpty()
				|| owner.getConsumerSecret().isEmpty();
		boolean reset = false;

		if (!missing) {
			setup.getConsole().say("An existing Consumer Token was found.");
			setup.getConsole().say(
					"Do you want to reset the Consumer Token anyway?");
			reset = setup.getConsole().prompt(BooleanPrompter.PROMPT_YESNO,
					false);
		}

		if (missing || reset) {
			String consumerKey = null;
			String consumerSecret = null;

			System.out.print("Please enter the application's Consumer Key: ");
			consumerKey = setup.getConsole().prompt(false);
			System.out
					.print("Please enter the application's Consumer Secret: ");
			consumerSecret = setup.getConsole().prompt(false);
			owner.setConsumerInfo(consumerKey, consumerSecret);
		}

		tw.setOAuthConsumer(owner.getConsumerKey(), owner.getConsumerSecret());
	}

	private void loadAccessToken(MEaterSetupConsole setup, TwitterStream tw) {
		AccessToken accessToken = null;

		final boolean missing = owner.getAccessToken().isEmpty()
				|| owner.getAccessTokenSecret().isEmpty();
		boolean reset = false;

		if (!missing) {
			setup.getConsole().say("An existing Access Token was found.");
			setup.getConsole().say(
					"Do you want to reset the Access Token anyway?");
			reset = setup.getConsole().prompt(BooleanPrompter.PROMPT_YESNO,
					false);
		}

		if (missing || reset) {
			RequestToken requestToken = null;

			try {
				requestToken = tw.getOAuthRequestToken();
			} catch (final TwitterException ex) {
				setup.getConsole().error(
						"Unable to get OAuth request token --\n%s",
						ex.getMessage());
				return;
			}

			setup.getConsole()
					.say("To create an Access Token, you must first log in to Twitter.");
			setup.getConsole()
					.say("Next, load the following site to grant access to this application: ");
			setup.getConsole().say("%s", requestToken.getAuthorizationURL());
			setup.getConsole().say(
					"Enter the PIN and press enter to continue: ");
			try {
				final String pin = setup.getConsole().prompt(true);
				if (pin.length() > 0) {
					accessToken = tw.getOAuthAccessToken(requestToken, pin);
				} else {
					accessToken = tw.getOAuthAccessToken();
				}
			} catch (final TwitterException ex) {
				setup.getConsole().error(
						"Unable to get OAuth request token --\n%s",
						ex.getMessage());
				return;
			}

			owner.setAccessInfo(accessToken.getToken(), accessToken
					.getTokenSecret());
		} else {
			accessToken = new AccessToken(owner.getAccessToken(), owner
					.getAccessTokenSecret());
		}

		tw.setOAuthAccessToken(accessToken);
	}

}
