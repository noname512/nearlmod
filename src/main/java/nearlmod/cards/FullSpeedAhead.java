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
    public static final String DEFMODE_DESCRIPTION = "造成 !D! 点伤害。 NL 切换至 nearlmod:无畏 。";
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
    public boolean extraTriggered() {
        return AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        if (extraTriggered()) {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new AtkStance()));
        }
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        baseDamage = AbstractDungeon.player.currentBlock;
        if (extraTriggered()) {
            baseDamage += TempHPField.tempHp.get(AbstractDungeon.player);
        }
        super.applyPowers();
        if (extraTriggered()) {
            damage += AtkStance.incNum;
            damage += AtkStance.atkInc;
            isDamageModified = (baseDamage != damage);
        }
        rawDescription = DESCRIPTION + UPGRADE_DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (extraTriggered()) {
            baseDamage += AtkStance.incNum;
            baseDamage += AtkStance.atkInc;
        }
        super.calculateCardDamage(mo);
        if (extraTriggered()) {
            baseDamage -= AtkStance.incNum;
            baseDamage -= AtkStance.atkInc;
            isDamageModified = (baseDamage != damage);
            rawDescription = DEFMODE_DESCRIPTION;
        } else {
            rawDescription = ATKMODE_DESCRIPTION;
        }
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new FullSpeedAhead();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
