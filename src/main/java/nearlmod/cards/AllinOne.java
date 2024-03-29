package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class AllinOne extends AbstractNearlCard {
    public static final String ID = "nearlmod:AllinOne";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "rhinemod/images/cards/allinone.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 4;
    private static final int BLOCK_AMT = 4;

    public AllinOne() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        block = baseBlock = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new GainBlockAction(p, p, block));
        if (p.stance.ID.equals(AtkStance.STANCE_ID)) {
            addToBot(new ChangeStanceAction(new DefStance()));
        } else {
            addToBot(new ChangeStanceAction(new AtkStance()));
        }
    }

    public void applyPowers() {
        AbstractPower tempDex = AbstractDungeon.player.getPower("Dexterity");
        AbstractPower tempStr = AbstractDungeon.player.getPower("Strength");
        int dex = 0, str = 0;
        if (tempDex != null) dex = tempDex.amount;
        if (tempStr != null) str = tempStr.amount;
        if (upgraded) {
            baseDamage += dex;
            baseBlock += str;
        }
        super.applyPowers();
        if (upgraded) {
            baseDamage -= dex;
            baseBlock -= str;
            isDamageModified = (damage != baseDamage);
            isBlockModified = (block != baseBlock);
        }
    }
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        AbstractPower tempDex = AbstractDungeon.player.getPower("Dexterity");
        AbstractPower tempStr = AbstractDungeon.player.getPower("Strength");
        int dex = 0, str = 0;
        if (tempDex != null) dex = tempDex.amount;
        if (tempStr != null) str = tempStr.amount;
        if (upgraded) {
            baseDamage += dex;
            baseBlock += str;
        }
        super.calculateCardDamage(mo);
        if (upgraded) {
            baseDamage -= dex;
            baseBlock -= str;
            isDamageModified = (damage != baseDamage);
            isBlockModified = (block != baseBlock);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new AllinOne();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
