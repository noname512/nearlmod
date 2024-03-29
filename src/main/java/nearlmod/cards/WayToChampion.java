package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.ExsanguinationPower;
import nearlmod.powers.FriendShelterPower;

public class WayToChampion extends AbstractNearlCard {
    public static final String ID = "nearlmod:WayToChampion";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "rhinemod/images/cards/waytochampion.png";
    private static final int COST = 4;
    private static final int UPGRADE_COST = 3;

    public WayToChampion() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FriendShelterPower(p)));
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters)
            if (!mo.isDeadOrEscaped())
                addToBot(new ApplyPowerAction(mo, p, new ExsanguinationPower(mo)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new WayToChampion();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
