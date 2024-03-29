package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.AtkStance;
import nearlmod.vfx.BraveTheDarknessEffect;

public class BraveTheDarkness extends AbstractNearlCard {
    public static final String ID = "nearlmod:BraveTheDarkness";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "rhinemod/images/cards/bravethedarkness.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_COST = 1;
    private static final int DECREASE_STRENGTH = 99;

    public BraveTheDarkness() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ENEMY);
        
        damage = baseDamage = ATTACK_DMG;
        exhaust = true;
        if (AbstractDungeon.player != null && extraTriggered()) {
            this.target = CardTarget.ALL_ENEMY;
            isMultiDamage = true;
        }
    }

    @Override
    public boolean extraTriggered() {
        return AbstractDungeon.player.stance != null && AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (extraTriggered()) {
            if (Settings.FAST_MODE) {
                CardCrawlGame.sound.play("BRAVE_THE_DARKNESS");
            } else {
                float lx = Settings.WIDTH, rx = 0.0F;
                float ly = Settings.HEIGHT, ry = 0.0F;
                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters)
                    if (!mo.isDeadOrEscaped()) {
                        lx = Math.min(lx, mo.drawX);
                        rx = Math.max(rx, mo.drawX);
                        ly = Math.min(ly, mo.drawY);
                        ry = Math.max(ry, mo.drawY);
                    }
                AbstractDungeon.effectList.add(new BraveTheDarknessEffect((lx + rx) / 2.0F - p.drawX, (ly + ry) / 2.0F - p.drawY));
                addToBot(new WaitAction(0.7F));
            }
            addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                if (!mo.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(mo, p, new StrengthPower(mo, -DECREASE_STRENGTH), -DECREASE_STRENGTH));
                    if (!mo.hasPower("Artifact")) {
                        addToBot(new ApplyPowerAction(mo, p, new GainStrengthPower(mo, DECREASE_STRENGTH), DECREASE_STRENGTH));
                    }
                }
            }
        } else {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -DECREASE_STRENGTH), -DECREASE_STRENGTH));
            if (m != null && !m.hasPower("Artifact")) {
                addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, DECREASE_STRENGTH), DECREASE_STRENGTH));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BraveTheDarkness();
    }

    @Override
    public void switchedStance() {
        if (extraTriggered()) {
            this.target = CardTarget.ALL_ENEMY;
            isMultiDamage = true;
        }
        else {
            this.target = CardTarget.ENEMY;
            isMultiDamage = false;
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
