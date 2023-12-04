package nearlmod.monsters;

import basemod.patches.com.megacrit.cardcrawl.rooms.AbstractRoom.EndBattleHook;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import nearlmod.actions.EndBattleAction;

public class KnightTerritoryHibernator extends AbstractMonster {
    public static final String ID = "nearlmod:KnightTerritoryHibernator";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "images/monsters/knightterritoryhibernator.png";
    public boolean asleep;
    private int level;

    public KnightTerritoryHibernator(float x, float y, int level) {
        super(NAME, ID, 180, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        this.level = level;
        type = EnemyType.ELITE;
        int dmg;
        if (AbstractDungeon.ascensionLevel >= 18) dmg = 18;
        else if (AbstractDungeon.ascensionLevel >= 3) dmg = 17;
        else dmg = 16;
        damage.add(new DamageInfo(this, MathUtils.floor(dmg * (1 + 0.1F * level))));
        int hp;
        if (AbstractDungeon.ascensionLevel >= 7) hp = 200;
        else hp = 180;
        setHp(MathUtils.floor(hp * (1 + 0.1F * level)));
        asleep = true;
    }

    @Override
    public void usePreBattleAction() {
    }

    @Override
    public void takeTurn() {
        if (nextMove == 1)
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        if (asleep && noOtherEnemy() && level == 0) {
            addToBot(new TalkAction(AbstractDungeon.player, DIALOG[1]));
            AbstractDungeon.actionManager.cleanCardQueue();
            AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
            AbstractDungeon.effectList.add(new DeckPoofEffect((float)Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
            AbstractDungeon.overlayMenu.hideCombatPanels();
            addToBot(new EndBattleAction());
        }
        getMove(0);
    }

    boolean noOtherEnemy() {
        for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!ms.id.equals("nearlmod:KnightTerritoryHibernator") && !ms.isDeadOrEscaped()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void getMove(int i) {
        if (asleep)
            setMove((byte)2, Intent.SLEEP);
        else
            setMove((byte)1, Intent.ATTACK, damage.get(0).base);
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (asleep && currentHealth < maxHealth) {
            AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[0]));
            asleep = false;
            setMove((byte)3, Intent.STUN);
            createIntent();
        }
    }
}
