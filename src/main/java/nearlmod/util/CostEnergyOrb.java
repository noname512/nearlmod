package nearlmod.util;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import nearlmod.characters.Nearl;

public class CostEnergyOrb extends CustomEnergyOrb {
    public static final float SECOND_ORB_IMG_SCALE = 0.75F * Settings.scale;
    public static final float PRIMARY_ORB_IMG_SCALE = 1.15F * Settings.scale;
    public static final float X_OFFSET = -20.0F * Settings.scale;
    public static final float Y_OFFSET = 150.0F * Settings.scale;
    public static final String[] orbTexturesAlt = {
            "images/char/orb/layer1alt.png",
            "images/char/orb/layer2alt.png",
            "images/char/orb/layer3alt.png",
            "images/char/orb/layer4alt.png",
            "images/char/orb/layer5alt.png",
            "images/char/orb/layer6.png",
            "images/char/orb/layer1d.png",
            "images/char/orb/layer2d.png",
            "images/char/orb/layer3d.png",
            "images/char/orb/layer4d.png",
            "images/char/orb/layer5d.png"
    };
    protected Texture secondBaseLayer;
    protected Texture[] secondEnergyLayers;
    protected Texture[] secondNoEnergyLayers;
    protected float[] secondLayerSpeeds;
    protected float[] secondAngles;
    private final Texture mask;
    private final FrameBuffer fbo;
    public CostEnergyOrb(String[] orbTexturePaths, String orbVfxPath, float[] layerSpeeds) {
        super(orbTexturePaths, orbVfxPath, layerSpeeds);
        int altIdx = 5;
        secondEnergyLayers = new Texture[altIdx];
        secondNoEnergyLayers = new Texture[altIdx];
        for (int i = 0; i < altIdx; i++) {
            secondEnergyLayers[i] = ImageMaster.loadImage(orbTexturesAlt[i]);
            secondNoEnergyLayers[i] = ImageMaster.loadImage(orbTexturesAlt[i + altIdx + 1]);
        }
        secondBaseLayer = ImageMaster.loadImage(orbTexturesAlt[5]);
        orbVfx = ImageMaster.loadImage(orbVfxPath);
        if (layerSpeeds == null) {
            layerSpeeds = new float[] { -20.0F, 20.0F, -40.0F, 40.0F, 360.0F };
        }
        secondLayerSpeeds = layerSpeeds;
        secondAngles = new float[this.secondLayerSpeeds.length];
        this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
        this.mask = ImageMaster.loadImage("images/char/orb/mask.png");
    }

    @Override
    public void updateOrb(int energyCount) {
        super.updateOrb(energyCount);
        int d = this.secondAngles.length;
        for (int i = 0; i < this.secondAngles.length; i++) {
            if (energyCount == 0) {
                this.secondAngles[i] = this.secondAngles[i] - Gdx.graphics.getDeltaTime() * this.secondLayerSpeeds[d - 1 - i] / 4.0F;
            } else {
                this.secondAngles[i] = this.secondAngles[i] - Gdx.graphics.getDeltaTime() * this.secondLayerSpeeds[d - 1 - i];
            }
        }

        if (secondVfxTimer != 0.0F) {
            this.secondEnergyVfxColor.a = Interpolation.exp10In.apply(0.5F, 0.0F, 1.0F - secondVfxTimer / 2.0F);
            this.secondEnergyVfxAngle += Gdx.graphics.getDeltaTime() * -30.0F;
            this.secondEnergyVfxScale = Settings.scale * Interpolation.exp10In.apply(1.0F, 0.1F, 1.0F - secondVfxTimer / 2.0F);
            secondVfxTimer -= Gdx.graphics.getDeltaTime();
            if (secondVfxTimer < 0.0F) {
                secondVfxTimer = 0.0F;
                this.secondEnergyVfxColor.a = 0.0F;
            }
        }
        hb.update();
    }

    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        hb.move(current_x + X_OFFSET, current_y + Y_OFFSET);
        sb.setColor(Color.WHITE);

        if (AbstractDungeon.player instanceof Nearl) {
            if (CostReserves.reserveCount() > 0) {
                for (int i = 0; i < this.secondEnergyLayers.length; i++) {
                    sb.draw(this.secondEnergyLayers[i], current_x + X_OFFSET - 64.0F, current_y + Y_OFFSET - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, SECOND_ORB_IMG_SCALE, SECOND_ORB_IMG_SCALE, this.secondAngles[i], 0, 0, 128, 128, false, false);
                }
            } else {
                for (int i = 0; i < this.secondNoEnergyLayers.length; i++) {
                    sb.draw(this.secondNoEnergyLayers[i], current_x + X_OFFSET - 64.0F, current_y + Y_OFFSET - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, SECOND_ORB_IMG_SCALE, SECOND_ORB_IMG_SCALE, this.secondAngles[i], 0, 0, 128, 128, false, false);
                }
            }
            sb.draw(this.secondBaseLayer, current_x + X_OFFSET - 64.0F, current_y + Y_OFFSET - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, SECOND_ORB_IMG_SCALE, SECOND_ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
        }
        sb.setColor(Color.WHITE);
        sb.end();
        this.fbo.begin();
        
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(16640);
        Gdx.gl.glColorMask(true, true, true, true);
        sb.begin();
        
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(770, 771);
        
        if (enabled) {
            for (int i = 0; i < this.energyLayers.length; i++) {
                sb.draw(this.energyLayers[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, PRIMARY_ORB_IMG_SCALE, PRIMARY_ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        } else {
            for (int i = 0; i < this.noEnergyLayers.length; i++) {
                sb.draw(this.noEnergyLayers[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, PRIMARY_ORB_IMG_SCALE, PRIMARY_ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        }
        
        if (hb.hovered && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
            TipHelper.renderGenericTip(50.0F * Settings.scale, 380.0F * Settings.scale, uiStrings.TEXT[0], uiStrings.TEXT[1]);
        }
        
        sb.setBlendFunction(0, 770);
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        sb.draw(this.mask, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, 1.0F, 1.0F, 0.0F, 0, 0, 128, 128, false, false);
        sb.setBlendFunction(770, 771);
        
        sb.end();
        
        this.fbo.end();
        sb.begin();
        TextureRegion drawTex = new TextureRegion(this.fbo.getColorBufferTexture());
        drawTex.flip(false, true);
        sb.draw(drawTex, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
        sb.draw(this.baseLayer, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, PRIMARY_ORB_IMG_SCALE, PRIMARY_ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
        hb.render(sb);
    }
    
    @SpirePatch(clz = AbstractPlayer.class, method = "<class>")
    public static class DoubleOrbField {
        public static SpireField<Boolean> isDoubleOrb = new SpireField<>(() -> Boolean.FALSE);
    }
    
    public static float secondVfxTimer = 0.0F;
    private static float secondEnergyVfxAngle = 0.0F;
    private static float secondEnergyVfxScale = Settings.scale;
    private static final Color secondEnergyVfxColor = Color.WHITE.cpy();

    private static final Hitbox hb = new Hitbox(80.0F * Settings.scale, 80.0F * Settings.scale);
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("nearlmod:CostEnergyOrb");
    
    @SpirePatch(clz = EnergyPanel.class, method = "renderVfx")
    public static class FlashSecondOrbPatch {
        @SpirePrefixPatch
        public static void flashSecondOrb(EnergyPanel __instance, SpriteBatch sb, Texture ___gainEnergyImg, Color ___energyVfxColor, float ___energyVfxScale, float ___energyVfxAngle) {
            if (DoubleOrbField.isDoubleOrb.get(AbstractDungeon.player) && CostEnergyOrb.secondVfxTimer > 0.0F) {
                sb.setBlendFunction(770, 1);
                sb.setColor(CostEnergyOrb.secondEnergyVfxColor);
                sb.draw(___gainEnergyImg, __instance.current_x + CostEnergyOrb.X_OFFSET - 128.0F, __instance.current_y + CostEnergyOrb.Y_OFFSET - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, CostEnergyOrb.secondEnergyVfxScale * CostEnergyOrb.SECOND_ORB_IMG_SCALE / CostEnergyOrb.PRIMARY_ORB_IMG_SCALE, CostEnergyOrb.secondEnergyVfxScale * CostEnergyOrb.SECOND_ORB_IMG_SCALE / CostEnergyOrb.PRIMARY_ORB_IMG_SCALE, CostEnergyOrb.secondEnergyVfxAngle - 50.0F, 0, 0, 256, 256, true, false);
                sb.draw(___gainEnergyImg, __instance.current_x + CostEnergyOrb.X_OFFSET - 128.0F, __instance.current_y + CostEnergyOrb.Y_OFFSET - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, CostEnergyOrb.secondEnergyVfxScale * CostEnergyOrb.SECOND_ORB_IMG_SCALE / CostEnergyOrb.PRIMARY_ORB_IMG_SCALE, CostEnergyOrb.secondEnergyVfxScale * CostEnergyOrb.SECOND_ORB_IMG_SCALE / CostEnergyOrb.PRIMARY_ORB_IMG_SCALE, -CostEnergyOrb.secondEnergyVfxAngle, 0, 0, 256, 256, false, false);
                sb.setBlendFunction(770, 771);
            }
        }
    }
}