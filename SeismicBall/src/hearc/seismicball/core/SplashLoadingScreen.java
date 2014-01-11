package hearc.seismicball.core;

import hearc.seismicball.framework.Game;
import hearc.seismicball.framework.Graphics;
import hearc.seismicball.framework.Screen;
import hearc.seismicball.framework.Graphics.ImageFormat;

public class SplashLoadingScreen extends Screen {
	
	private static final String splashFileName = "splash.jpg";
	
	public SplashLoadingScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		// TODO Assets.splash = g.newImage(splashFileName, ImageFormat.RGB565);
		
		game.setScreen(new LoadingScreen(game));
	}

	@Override
	public void paint(float deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void backButton() {
		// TODO Auto-generated method stub

	}

}
