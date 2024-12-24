package nearlmod.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import nearlmod.actions.EndBattleAction;

public class KnightTerritoryHibernator extends AbstractMonster {
    public static final String ID = "nearlmod:KnightTerritoryHibernator";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "resources/nearlmod/images/monsters/knightterritoryhibernator.png";
    public boolean asleep;

    public KnightTerritoryHibernator(float x, float y, int level) {
        super(NAME, ID, 180, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
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
        loadAnimation("resources/nearlmod/images/monsters/enemy_1181_napkgt_2/enemy_1181_napkgt_233.atlas", "resources/nearlmod/images/monsters/enemy_1181_napkgt_2/enemy_1181_napkgt_233.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Idle_Sleep", true);
    }

    @Override
    public void takeTurn() {
        if (nextMove == 1) {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0);
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        } if (asleep && noOtherEnemy()) {
            addToBot(new TalkAction(AbstractDungeon.player, DIALOG[1]));
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
        int previousHealth = this.currentHealth;
        super.damage(info);
        if (asleep && currentHealth < previousHealth) {
            this.state.setAnimation(0, "Idle", true);
            addToBot(new ShoutAction(this, DIALOG[0]));
            asleep = false;
            setMove((byte)3, Intent.STUN);
            createIntent();
        }
    }
}
