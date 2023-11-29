package nearlmod.monsters;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.BronzeOrb;
import nearlmod.actions.SummonOrbAction;
import nearlmod.orbs.Blemishine;
import nearlmod.powers.HintPower;

import java.util.Iterator;

public class NightzmoraImitator extends AbstractMonster {
    public static final String ID = "nearlmod:NightzmoraImitator";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "images/monsters/nightzmoraimitator.png";

    public NightzmoraImitator(float x, float y) {
        super(NAME, ID, 30, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 7)
            setHp(35);
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.damage.add(new DamageInfo(this, 10));
            this.damage.add(new DamageInfo(this, 8));
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            this.damage.add(new DamageInfo(this, 9));
            this.damage.add(new DamageInfo(this, 7));
        } else {
            this.damage.add(new DamageInfo(this, 8));
            this.damage.add(new DamageInfo(this, 6));
        }
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
        }
        else {
            int def_val = AbstractDungeon.player.currentBlock + TempHPField.tempHp.get(AbstractDungeon.player);
            if (this.damage.get(1).output > def_val) {
                // TODO: 背刺动画，攻击在最后一个伙伴上
                addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, def_val)));
                this.addToBot(new DecreaseMaxOrbAction(1));
            }
            else {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
            }
        }
        byte nextIntent;
        for (;;) {
            nextIntent = (byte) AbstractDungeon.aiRng.random(0, 2);
            if (nextIntent == this.nextMove) {
                continue;
            }
            if (nextIntent <= 1) {
                setMove(nextIntent, Intent.ATTACK, this.damage.get(0).base);
            }
            else {
                if (!AbstractDungeon.player.hasOrb()) {
                    continue;
                }
                setMove(MOVES[0], nextIntent, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
            }
            break;
        }
    }

    @Override
    protected void getMove(int i) {
        if ((AbstractDungeon.aiRng.random(0, 1) == 0) && (AbstractDungeon.player.hasOrb())) {
            setMove((byte)2, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
        }
        else {
            setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
        }
    }
}
