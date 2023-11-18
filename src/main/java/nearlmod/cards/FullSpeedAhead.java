package nearlmod.cards;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class FullSpeedAhead extends AbstractNearlCard {
    public static final String ID = "nearlmod:FullSpeedAhead";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String ATKMODE_DESCRIPTION = "造成 !D! 点伤害。";
    public static final String DEFMODE_DESCRIPTION = "造成 !D! 点伤害。 NL 切换至 nearlmod:剑枪形态 。";
    public static final String IMG_PATH = "images/cards/nearlstrike.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public FullSpeedAhead() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.ENEMY);
        
        damage = baseDamage = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        if (AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new AtkStance()));
        }
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void applyPowers() {
        this.baseDamage = AbstractDungeon.player.currentBlock;
        if (AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) {
            this.baseDamage += TempHPField.tempHp.get(AbstractDungeon.player);
        }
        super.applyPowers();
        if (AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) {
            this.damage += AtkStance.incNum;
            this.damage += AtkStance.atkInc;
            isDamageModified = (baseDamage != damage);
        }
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription + cardStrings.UPGRADE_DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) {
            this.baseDamage += AtkStance.incNum;
            this.baseDamage += AtkStance.atkInc;
        }
        super.calculateCardDamage(mo);
        if (AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) {
            this.baseDamage -= AtkStance.incNum;
            this.baseDamage -= AtkStance.atkInc;
            isDamageModified = (baseDamage != damage);
            this.rawDescription = DEFMODE_DESCRIPTION;
        }
        else {
            this.rawDescription = ATKMODE_DESCRIPTION;
        }
        this.initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new FullSpeedAhead();
    }

    public void triggerOnGlowCheck() {
        if (AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
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
