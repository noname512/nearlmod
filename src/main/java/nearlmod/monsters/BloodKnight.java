package nearlmod.monsters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import nearlmod.powers.AttackUpPower;
import nearlmod.powers.DuelPower;
import nearlmod.powers.ExsanguinationPower;
import nearlmod.powers.RebornPower;

public class BloodKnight extends AbstractMonster {
    public static final String ID = "nearlmod:BloodKnight";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "images/monsters/bloodknight.png";

    public static final float[] POSX = new float[] { 195.0F, -235.0F, 165.0F, -265.0F, 225.0F, -205.0F };
    public static final float[] POSY = new float[] { 85.0F, 75.0F, 225.0F, 215.0F, 345.0F, 335.0F };
    private int debuffTimes = 2;
    private int bladeStrength = 0;
    private int currentTurn;
    private boolean isStage2;
    private final AbstractMonster[] blades = new AbstractMonster[6];
    private final int bladesPerSpawn;
    private boolean talked;
    private int realHealth;

    public BloodKnight(float x, float y) {
        super(NAME, ID, 300, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 19) {
            damage.add(new DamageInfo(this, 20));
            damage.add(new DamageInfo(this, 12));
            debuffTimes = 3;
        }
        else if (AbstractDungeon.ascensionLevel >= 4) {
            damage.add(new DamageInfo(this, 20));
            damage.add(new DamageInfo(this, 12));
        }
        else {
            damage.add(new DamageInfo(this, 18));
            damage.add(new DamageInfo(this, 12));
        }
        if (AbstractDungeon.ascensionLevel >= 15) {
            bladeStrength = 2;
        }
        if (AbstractDungeon.ascensionLevel >= 9)
            setHp(340);
        if (AbstractDungeon.ascensionLevel >= 19)
            bladesPerSpawn = 2;
        else
            bladesPerSpawn = 1;
        currentTurn = 0;
        isStage2 = false;
        talked = false;
        loadAnimation("images/monsters/enemy_1524_bldkgt/enemy_1524_bldkgt33.atlas", "images/monsters/enemy_1524_bldkgt/enemy_1524_bldkgt33.json", 1.5F);
        this.state.setTimeScale(2.0F);
        this.flipHorizontal = true;
        this.stateData.setMix("A_Idle", "A_Die_Start", 0.1F);
        this.state.setAnimation(0, "A_Idle", true);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new TalkAction(this, DIALOG[0], 0.3F, 3.0F));
        addToBot(new ApplyPowerAction(this, this, new DuelPower(this, 25)));
        addToBot(new ApplyPowerAction(this, this, new RebornPower(this)));
        AbstractDungeon.getCurrRoom().cannotLose = true;
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (currentHealth <= 0 && !this.halfDead) {
            if (AbstractDungeon.getCurrRoom().cannotLose) {
                halfDead = true;
                addToBot(new HealAction(this, this, MathUtils.floor(this.maxHealth * 0.4F)));
                for (AbstractPower p : this.powers)
                    p.onDeath();
                for (AbstractRelic r : AbstractDungeon.player.relics)
                    r.onMonsterDeath(this);
            }

            addToTop(new ClearCardQueueAction());
            setMove((byte)50, Intent.UNKNOWN);
            createIntent();
            applyPowers();
        }
    }

    private void setAttackAnim() {
        if (isStage2) {
            this.state.setAnimation(0, "B_Attack", false);
            this.state.addAnimation(0, "B_Idle", true, 0);
        } else {
            this.state.setAnimation(0, "A_Attack", false);
            this.state.addAnimation(0, "A_Idle", true, 0);
        }
        CardCrawlGame.sound.play("BLOOD_KNIGHT_ATTACK");
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (this.nextMove == 1) {
            setAttackAnim();
            addToBot(new DamageAction(p, damage.get(0)));
        }
        else if (this.nextMove == 2) {
            setAttackAnim();
            addToBot(new DamageAction(p, damage.get(1)));
            addToBot(new ApplyPowerAction(p, this, new WeakPower(p, debuffTimes, true)));
            addToBot(new ApplyPowerAction(p, this, new VulnerablePower(p, debuffTimes, true)));
        }
        else if (this.nextMove == 3) {
            cleanDebuff();
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 3)));
        }
        else if (this.nextMove == 4) {
            cleanDebuff();
            spawnBlade(bladesPerSpawn);
        }
        else if (this.nextMove == 99) {
            CardCrawlGame.sound.play("BLOOD_KNIGHT_SKILL");
            if (isStage2) {
                this.state.setAnimation(0, "B_Skill", false);
                this.state.addAnimation(0, "B_Idle", true, 0);
            } else {
                this.state.setAnimation(0, "A_Skill", false);
                this.state.addAnimation(0, "A_Idle", true, 0);
            }
            if (!talked) {
                addToBot(new TalkAction(this, DIALOG[1], 0.3F, 3.0F));
                talked = true;
            }
            addToBot(new ApplyPowerAction(p, this, new ExsanguinationPower(p)));
        }
        else if (this.realHealth == this.maxHealth) {
            CardCrawlGame.sound.play("BLOOD_KNIGHT_REBORN_LAS");
            this.state.setAnimation(0, "A_Die_End", false);
            this.state.addAnimation(0, "A_Idle", true, 0);
            this.currentHealth = this.realHealth;
            this.halfDead = false;
            cleanDebuff();
            currentTurn = -1;
        }
        else if (this.nextMove == 55) {
            CardCrawlGame.sound.play("BLOOD_KNIGHT_REBORN_NXT");
            loadAnimation("images/monsters/enemy_1524_bldkgt/enemy_1524_bldkgt33.atlas", "images/monsters/enemy_1524_bldkgt/enemy_1524_bldkgt33.json", 1.5F);
            this.state.setTimeScale(2.0F);
            this.stateData.setMix("B_Idle", "B_Die", 0.1F);
            this.state.setAnimation(0, "A_Die_End_2", false);
            this.state.addAnimation(0, "B_Idle", true, 0);
            this.currentHealth = this.realHealth;
            this.halfDead = false;
            cleanDebuff();
            currentTurn = -1;
            isStage2 = true;
            addToBot(new CanLoseAction());
            addToBot(new TalkAction(this, DIALOG[2], 0.3F, 3.0F));
            addToBot(new RemoveSpecificPowerAction(this, this, "nearlmod:Reborn"));
            addToBot(new ApplyPowerAction(this, this, new DuelPower(this, 25)));
            addToBot(new ApplyPowerAction(this, this, new AttackUpPower(this, 50)));
        }
        else {
            spawnBlade(bladesPerSpawn + 1);
            setMove((byte)(this.nextMove + 1), Intent.UNKNOWN);
            return;
        }
        currentTurn ++;
        if ((currentTurn == 2) && (!p.hasPower("nearlmod:Exsanguination"))) {
            setMove(MOVES[0], (byte)99, Intent.STRONG_DEBUFF);
        }
        else {
            for (;;) {
                byte next = (byte)AbstractDungeon.aiRng.random(0, 2);
                if ((next == this.nextMove) || ((next == 0) && (this.nextMove > 2))) {
                    continue;
                }
                if (next == 1) {
                    setMove(next, Intent.ATTACK, this.damage.get(0).base);
                }
                else if (next == 2) {
                    setMove(next, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                }
                else if (next == 0) {
                    if (!isStage2) {
                        setMove((byte)3, Intent.BUFF);
                    }
                    else {
                        setMove((byte)4, Intent.BUFF);
                    }
                }
                break;
            }
        }
    }

    private void spawnBlade(int spawnCnt) {
        int spawned = 0;
        for (int i = 0; spawned < spawnCnt && i < blades.length; i++)
            if (blades[i] == null || blades[i].isDeadOrEscaped()) {
                BloodBlade bloodBlade = new BloodBlade(POSX[i], POSY[i], bladeStrength);
                blades[i] = bloodBlade;
                addToBot(new SpawnMonsterAction(bloodBlade, true));
                spawned++;
            }
    }

    private void cleanDebuff() {
        for (AbstractPower pow : powers)
            if (pow != null && pow.type == AbstractPower.PowerType.DEBUFF) {
                addToBot(new RemoveSpecificPowerAction(this, this, pow));
            }
    }

    @Override
    protected void getMove(int i) {
        byte next = (byte)AbstractDungeon.aiRng.random(0, 2);
        if (next == 1) {
            setMove(next, Intent.ATTACK, this.damage.get(0).base);
        }
        else if (next == 2) {
            setMove(next, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
        }
        else if (next == 0) {
            setMove((byte)3, Intent.BUFF);
        }
    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            this.state.setAnimation(0, "B_Die", false);
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                if (!m.isDeadOrEscaped()) {
                    addToTop(new HideHealthBarAction(m));
                    addToTop(new SuicideAction(m));
                    addToTop(new VFXAction(m, new InflameEffect(m), 0.2F));
                }
            super.die();
        } else {
            this.state.setAnimation(0, "A_Die_Start", false);
            this.state.addAnimation(0, "A_Die_Loop", true, 0);
        }
    }

    @Override
    public void heal(int healAmount) {
        if (halfDead) {
            for (AbstractPower p : powers)
                healAmount = p.onHeal(healAmount);

            realHealth += healAmount;
            if (realHealth > maxHealth) {
                realHealth = maxHealth;
            }

            if (healAmount > 0) {
                AbstractDungeon.effectList.add(new HealEffect(hb.cX - animX, hb.cY, healAmount));
                healthBarUpdatedEvent();
            }
        } else {
            super.heal(healAmount);
        }
    }

    @Override
    public void renderHealth(SpriteBatch sb) {
        if (halfDead) currentHealth = realHealth;
        super.renderHealth(sb);
        if (halfDead) currentHealth = 0;
    }

    @Override
    public void healthBarUpdatedEvent() {
        if (halfDead) currentHealth = realHealth;
        super.healthBarUpdatedEvent();
        if (halfDead) currentHealth = 0;
    }

    @Override
    public void healthBarRevivedEvent() {
        if (halfDead) currentHealth = realHealth;
        super.healthBarRevivedEvent();
        if (halfDead) currentHealth = 0;
    }
}
