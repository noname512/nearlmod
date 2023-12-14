package nearlmod.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.actions.TrueDieAction;
import nearlmod.powers.LightPower;

public class CandleKnight extends AbstractMonster {
    public static final String ID = "nearlmod:CandleKnight";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "images/monsters/candleknight.png";
    private final int damageTimes;
    private int said = 0;

    public CandleKnight() {
        super(NAME, ID, 120, 0, 0, 150.0F, 320.0F, IMAGE);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8)
            setHp(140);
        int candleDmg, flameDmg, swordDmg;
        if (AbstractDungeon.ascensionLevel >= 18) {
            candleDmg = 12;
            flameDmg = 40;
            swordDmg = 6;
            damageTimes = 3;
        } else if (AbstractDungeon.ascensionLevel >= 3) {
            candleDmg = 11;
            flameDmg = 30;
            swordDmg = 6;
            damageTimes = 3;
        } else {
            candleDmg = 10;
            flameDmg = 30;
            swordDmg = 6;
            damageTimes = 2;
        }
        this.damage.add(new DamageInfo(this, candleDmg));
        this.damage.add(new DamageInfo(this, flameDmg));
        this.damage.add(new DamageInfo(this, swordDmg));
        loadAnimation("images/monsters/enemy_1184_cadkgt_3/enemy_1184_cadkgt_333.atlas", "images/monsters/enemy_1184_cadkgt_3/enemy_1184_cadkgt_333.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.stateData.setMix("Skill_Loop", "Skill_End", 0.1F);
        this.state.setAnimation(0, "Idle", true);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new TalkAction(AbstractDungeon.player, DIALOG[2], 0.3F, 3.0F));
        addToBot(new TalkAction(this, DIALOG[0], 2.5F, 3.0F));
        addToBot(new TalkAction(this, DIALOG[1], 0.3F, 3.0F));
    }

    public void changeImg(String imgUrl) {
        this.img = new Texture(imgUrl);
    }

    @Override
    public void takeTurn() {
        if (!this.isDying && this.currentHealth <= this.maxHealth * 0.3F && this.said == 1) {
            this.said = 2;
            addToBot(new TalkAction(AbstractDungeon.player, DIALOG[5], 2.0F, 3.0F));
            addToBot(new TalkAction(this, DIALOG[6], 0.3F, 3.0F));
        }
        if (!this.isDying && this.currentHealth <= this.maxHealth * 0.6F && this.said == 0) {
            this.said = 1;
            addToBot(new TalkAction(this, DIALOG[3], 2.0F, 3.0F));
            addToBot(new TalkAction(AbstractDungeon.player, DIALOG[4], 0.3F, 3.0F));
        }
        if (this.nextMove == 2) {
            this.state.setAnimation(0, "Skill_Begin", false);
            this.state.addAnimation(0, "Skill_Loop", true, 0.0F);
            CardCrawlGame.sound.playAndLoop("CANDLE_KNIGHT_CHARGE");
            setMove(MOVES[1], (byte) 3, Intent.ATTACK, this.damage.get(1).base);
        } else {
            if (this.nextMove == 1) {
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
            } else if (this.nextMove == 4) {
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
            } else if (this.nextMove == 3) {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                CardCrawlGame.sound.stop("CANDLE_KNIGHT_CHARGE");
                CardCrawlGame.sound.play("CANDLE_KNIGHT_ATTACK");
                this.state.setAnimation(0, "Skill_End", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
            } else if (this.nextMove == 5) {
                addToBot(new AnimateFastAttackAction(this));
                for (int i = 1; i <= damageTimes; i++)
                    addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            } else if (this.nextMove == 6) {
                AbstractPower power = AbstractDungeon.player.getPower(LightPower.POWER_ID);
                if (power != null) {
                    int amount = power.amount;
                    if (AbstractDungeon.ascensionLevel >= 18) amount = MathUtils.ceil(amount / 2.0F);
                    else amount = MathUtils.ceil(amount / 3.0F);
                    addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, amount)));
                    addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, this, power));
                }
            }
            if (this.nextMove == 1 || AbstractDungeon.aiRng.random(0, 5) == 0) {
                setMove(MOVES[0], (byte) 2, Intent.UNKNOWN);
                return;
            }
            if (AbstractDungeon.ascensionLevel >= 15 &&
                AbstractDungeon.player.hasPower(LightPower.POWER_ID) &&
                AbstractDungeon.aiRng.random(0, 5) == 0) {
                setMove(MOVES[3], (byte)6, Intent.BUFF);
                return;
            }
            int randInt = AbstractDungeon.aiRng.random(0, 1);
            if (randInt == 0)
                setMove((byte)4, Intent.ATTACK, this.damage.get(0).base);
            else
                setMove(MOVES[2], (byte)5, Intent.ATTACK, this.damage.get(2).base, damageTimes, true);
        }
    }

    @Override
    public void die() {
        CardCrawlGame.sound.stop("CANDLE_KNIGHT_CHARGE");
        this.state.setAnimation(0, "Die", false);
        addToBot(new TalkAction(this, DIALOG[7], 0.3F, 3.0F));
        addToBot(new WaitAction(3.0F));
        addToBot(new TrueDieAction(this));
    }

    public void trueDie() {
        super.die();
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
    }
}
