package nearlmod.characters;

import nearlmod.NLMOD;
import basemod.abstracts.CustomPlayer;

import java.util.ArrayList;

public class Nearl extends CustomPlayer {
    private static final Color NearlGold = CardHelper.getColor(255, 236, 194);
    public static final String ID = "Nearl";
    public static final String TACHI = "images/char/tachi.png";
    public static final String SHOULDER = null;//"images/char/shoulder.png";
    public static final String CORPSE = "images/char/corpse.png";
    public static final String[] orbTextures = {
        "images/char/orb.png"
    };

    public Nearl() {
        // 参数列表：角色名，角色类枚举，能量面板贴图路径列表，能量面板特效贴图路径，能量面板贴图旋转速度列表，能量面板，模型资源路径，动画资源路径
        super(ID, NearlEnum.NEARL_CLASS, orbTextures, null, null, null);
        
        // 对话框位置，默认就好
        dialogX = drawX + 0.0F * Settings.scale;
        dialogY = drawY + 220.0F * Settings.scale;

        // 参数列表：静态贴图路径，越肩视角2贴图路径，越肩视角贴图路径，失败时贴图路径，角色选择界面信息，碰撞箱XY宽高，初始能量数
        initializeClass(TACHI, SHOULDER, SHOULDER, CORPSE, getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(START_ENERGY));
    }

    @Override
    public Color getSlashAttackColor() {
        return NearlGold;
    }

    @Override
    public Color getCardRenderColor() {
        return NearlGold;
    }

    @Override
    public Color getCardTrailColor() {
        return NearlGold;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.NEARL_GOLD;
    }

    @Override
    public String getSpireHeartText() {
        return "NL 你握紧了手中的剑枪和骑士盾……";
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return "玛嘉烈·临光";
    }
    
    @Override
    public String getLocalizedCharacterName() {
        return "玛嘉烈·临光";
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Nearl(name);
    }

    public ArrayList<String> getStartingDeck() {
        ArrayList<String> ret = new ArrayList<String>();
        ret.add(NearlStrike.ID);
        ret.add(NearlStrike.ID);
        ret.add(NearlStrike.ID);
        ret.add(NearlStrike.ID);
        ret.add(NearlDefend.ID);
        ret.add(NearlDefend.ID);
        ret.add(NearlDefend.ID);
        ret.add(NearlDefend.ID);
        // ret.add(SwitchType.ID);
        // ret.add(MajestyLight.ID);
        return ret;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> ret = new ArrayList<String>();
        return ret;
    }
    
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                77, 77, 0, 99, 5, //starting hp, max hp, max orbs, starting gold, starting hand size
                this, getStartingRelics(), getStartingDeck(), false);
    }
}