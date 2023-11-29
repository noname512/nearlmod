package nearlmod.monsters;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import nearlmod.actions.SummonOrbAction;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.cards.special.BlemishinesFaintLight;
import nearlmod.orbs.Blemishine;
import nearlmod.powers.HintPower;

public class LastKheshig extends AbstractMonster {
    public static final String ID = "nearlmod:LastKheshig";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "images/monsters/lastkheshig.png";

    public static final float[] POSX = new float[] { 195.0F, -235.0F };
    public static final float[] POSY = new float[] { 0.0F, 0.0F };
    private final AbstractMonster[] imitators = new AbstractMonster[2];
    private boolean talked;
    public static boolean isBlemishineSurvive;

    public LastKheshig(float x, float y) {
        super(NAME, ID, 160, 25.0F, 0, 150.0F, 320.0F, IMAGE, x, y);
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
        talked = false;
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new TalkAction(this, DIALOG[0], 0.3F, 3.0F));
        addToBot(new ApplyPowerAction(this, this, new HintPower(this, 0)));
        addToBot(new SummonOrbAction(new Blemishine()));
        if (AbstractDungeon.ascensionLevel >= 15)
            spawnImitator();
    }

    @Override
    public void takeTurn() {
        if (this.nextMove <= 1) {
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
        } else if (this.nextMove == 2) {
            if (!talked) {
                addToBot(new TalkAction(this, DIALOG[1], 0.3F, 3.0F));
                talked = true;
            }
            spawnImitator();
            if (AbstractDungeon.ascensionLevel >= 18) {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                    if (m != null && !m.isDying) {
                        addToBot(new GainBlockAction(m, 6));
                    }
            }
        } else {
            int def_val = AbstractDungeon.player.currentBlock + TempHPField.tempHp.get(AbstractDungeon.player);
            if (this.damage.get(1).output > def_val) {
                // TODO: 背刺动画，攻击在最后一个伙伴上
                addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, def_val)));
                if (AbstractDungeon.player.orbs.size() <= 1)
                    isBlemishineSurvive = false;
                this.addToBot(new DecreaseMaxOrbAction(1));
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
                if (fullImitator()) continue;
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

    private void spawnImitator() {
        for (int i = 0; i < imitators.length; i++)
            if (imitators[i] == null || imitators[i].isDeadOrEscaped()) {
                NightzmoraImitator imitator = new NightzmoraImitator(POSX[i], POSY[i]);
                imitators[i] = imitator;
                addToBot(new SpawnMonsterAction(imitator, true));
                break;
            }
    }

    private boolean fullImitator() {
        for (int i = 1; i < imitators.length; i++)
            if (imitators[i] == null || imitators[i].isDeadOrEscaped())
                return false;
        return true;
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
            AbstractNearlCard.addSpecificCardsToReward(new BlemishinesFaintLight());
        }
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
            if (!m.isDeadOrEscaped()) {
                addToTop(new HideHealthBarAction(m));
                addToTop(new SuicideAction(m));
                addToTop(new VFXAction(m, new InflameEffect(m), 0.2F));
            }
        super.die();
    }
}
