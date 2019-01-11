package mygdxgame;

import interfaces.DrawableEntity;
import interfaces.Obstacle;
import interfaces.Targetable;
import interfaces.Updateable;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Pool;

public class MyGdxGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shaper;
	private HumanPlayer player;
	private ComputerPlayer aiOpponent;
	private ArrayList<Targetable> targetList;
	private ArrayList<DrawableEntity> drawList;
	private ArrayList<Obstacle> obstacleList;
	private ArrayList<Updateable> updateList;
	private ArrayList<Projectile> bulletList;
	private Pool<Projectile> bulletPool;
	private TerrainGenerator terrainGenerator;
	private Pathfinder pFinder;
	private GameInputProcessor gameInput;
	private Stage uiStage;
	private HUD userInterface;
	private InputMultiplexer multiplexer;
	private ArrayList<Node> nodeList;
	private AssetManager manager;
	private boolean flag = true;
	private Sprite background;
	private int width = 800;
	private int height = 600;

	@Override
	public void create() {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(width, height);
		camera.setToOrtho(true);
		batch = new SpriteBatch();
		shaper = new ShapeRenderer();

		uiStage = new Stage();
		Table table = new Table();
		table.setFillParent(true);
		table.left().top();
		uiStage.addActor(table);

		userInterface = new HUD(uiStage, table);

		manager = new AssetManager();
		manager.load("data/test.png", Texture.class);
		manager.load("data/image.png", Texture.class);
		manager.load("data/libgdx.png", Texture.class);
		manager.load("data/demo.png", Texture.class);
		manager.load("data/tower.png", Texture.class);
		manager.load("data/turret.png", Texture.class);
		manager.load("data/robot.png", Texture.class);
		manager.load("data/robotBody.png", Texture.class);
		manager.load("data/nodeSquare.png", Texture.class);
		manager.load("data/route.png", Texture.class);
		manager.load("data/rock.png", Texture.class);
		manager.load("data/blank.png", Texture.class);
		manager.load("data/brown.png", Texture.class);
		manager.load("data/baddie.png", Texture.class);

		drawList = new ArrayList<DrawableEntity>();
		obstacleList = new ArrayList<Obstacle>();
		updateList = new ArrayList<Updateable>();
		targetList = new ArrayList<Targetable>();

		bulletList = new ArrayList<Projectile>();
		bulletPool = new Pool<Projectile>() {
			@Override
			protected Projectile newObject() {
				return new Projectile(manager.get("data/test.png", Texture.class), bulletList, drawList, targetList, obstacleList);
			}
		};

		pFinder = new Pathfinder(width, height);
		nodeList = pFinder.getNodeList();

		player = new HumanPlayer(shaper, targetList, drawList, updateList, bulletPool, manager, pFinder);
		aiOpponent = new ComputerPlayer(targetList, drawList, updateList, bulletPool, manager, pFinder);

		terrainGenerator = new TerrainGenerator(obstacleList, drawList, nodeList, manager);

		multiplexer = new InputMultiplexer();
		gameInput = new GameInputProcessor(player.getOrderGenerator());
		multiplexer.addProcessor(uiStage);
		multiplexer.addProcessor(gameInput);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void dispose() {
		batch.dispose();
		manager.dispose();
		shaper.dispose();
		uiStage.dispose();
		userInterface.dispose();
	}

	@Override
	public void render() {
		ArrayList<Updateable> dieList = new ArrayList<Updateable>(); 
		for(Updateable updater : updateList) {
			updater.update();
			if(!updater.isAlive()) {
				dieList.add(updater);
			}
		}
		for(Updateable thing : dieList) {
			thing.die(updateList, targetList);
		}
		dieList.clear();
		for(int i = 0; i<bulletList.size(); i++) {
			Projectile bullet = bulletList.get(i);
			bullet.update();
			if(!bullet.isAlive()) {
				bulletPool.free(bullet);
			}
		}

		if(manager.update() && flag) {
			if(background==null) {
				Texture tex = manager.get("data/brown.png", Texture.class);
				TextureRegion region = new TextureRegion(tex, width, height);
				background = new Sprite(region);
			}
			bulletPool.clear();
			terrainGenerator.createTerrain();

			for(Obstacle obstacle : obstacleList) {
				obstacle.findBlockedNodes(nodeList);
			}

			player.setup();
			aiOpponent.setup();
			flag = false;
		} else {
			if(flag) {
				float progress = (manager.getProgress() * 100);
				System.out.println("Assets loaded: " + progress + "%");
			}
		}

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if(background!=null) {
			background.draw(batch);
		}
		for (DrawableEntity drawer : drawList) {
			drawer.draw(batch);
		}
		batch.end();
		player.drawShapes(camera.combined);
		shaper.begin(ShapeType.FilledCircle);
		for(Node node : nodeList) {
			node.drawShapes(camera.combined, shaper);
		}
		shaper.end();

		uiStage.act(Gdx.graphics.getDeltaTime());
		uiStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(true);
		uiStage.setViewport(width, height, true);
		if(background!=null) {
			Texture tex = manager.get("data/brown.png", Texture.class);
			TextureRegion region = new TextureRegion(tex, width, height);
			background = new Sprite(region);
		}
		pFinder.createNodeList(width, height);
		nodeList = pFinder.getNodeList();
		for(Obstacle obstacle : obstacleList) {
			obstacle.findBlockedNodes(nodeList);
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
