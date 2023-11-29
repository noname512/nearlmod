package nearlmod.monsters;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.BronzeOrb;
import nearlmod.actions.SummonOrbAction;
import nearlmod.orbs.Blemishine;
import nearlmod.powers.HintPower;

import java.util.Iterator;

public class LastKheshig extends AbstractMonster {
    public static final String ID = "nearlmod:LastKheshig";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "images/monsters/lastkheshig.png";

    public boolean isBlemishineSurvive;

    public LastKheshig(float x, float y) {
        super(NAME, ID, 160, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8)
            setHp(180);
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.damage.add(new DamageInfo(this, 24));
            this.damage.add(new DamageInfo(this, 19));
        } else if (AbstractDungeon.ascensionLevel >= 3) {
            this.damage.add(new DamageInfo(this, 22));
            this.damage.add(new DamageInfo(this, 17));
        } else {
            this.damage.add(new DamageInfo(this, 20));
            this.damage.add(new DamageInfo(this, 16));
        }
        isBlemishineSurvive = true;
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new HintPower(this, 0)));
        AbstractDungeon.actionManager.addToBottom(new SummonOrbAction(new Blemishine()));
    }

    @Override
    public void takeTurn() {
        if (this.nextMove <= 1) {
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
        } else if (this.nextMove == 2) {
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new NightzmoraImitator(-300.0F, 200.0F), true));
            // TODO: 调位置
        } else {
            int def_val = AbstractDungeon.player.currentBlock + TempHPField.tempHp.get(AbstractDungeon.player);
            if (this.damage.get(1).output > def_val) {
                // TODO: 背刺动画，攻击在最后一个伙伴上
                addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, def_val)));
                this.addToBot(new DecreaseMaxOrbAction(1));
                if (!AbstractDungeon.player.hasOrb()) {
                    isBlemishineSurvive = false;
                }
            } else {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
            }
        }
        byte nextIntent;
        for (; ; ) {
            nextIntent = (byte) AbstractDungeon.aiRng.random(0, 3);
            if (nextIntent == this.nextMove) {
                continue;
            }
            if (nextIntent <= 1) {
                setMove(nextIntent, Intent.ATTACK, this.damage.get(0).base);
            } else if (nextIntent == 2) {
                if (numAliveImitaor() >= 2) {
                    continue;
                }
                setMove(nextIntent, Intent.UNKNOWN);
            } else {
                if (!AbstractDungeon.player.hasOrb()) {
                    continue;
                }
                setMove(MOVES[0], nextIntent, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
            }
            break;
        }
    }

    private int numAliveImitaor() {
        int count = 0;
        Iterator var2 = AbstractDungeon.getMonsters().monsters.iterator();

        while (var2.hasNext()) {
            AbstractMonster m = (AbstractMonster) var2.next();
            if (m != null && m != this && !m.isDying) {
                ++count;
            }
        }

        return count;
    }

    @Override
    protected void getMove(int i) {
        if (AbstractDungeon.aiRng.random(0, 1) == 0) {
            setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        } else {
            setMove((byte) 3, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
        }
    }

    @Override
    public void die() {
        if (isBlemishineSurvive) {
            //TODO: 加奖励
        }
        super.die();
    }
}
