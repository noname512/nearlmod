package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.orbs.Nightingale;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.WFPPower;

public class WhiteFiendProtection extends AbstractFriendCard {
    public static final String ID = "nearlmod:WhiteFiendProtection";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/flashfade.png";
    private static final int COST = 2;
    public static final String BG_IMG = "images/512/bg_friend_test.png";

    public WhiteFiendProtection() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Nightingale");
        this.bannerSmallRegion = ImageMaster.CARD_BANNER_RARE;
        this.bannerLargeRegion = ImageMaster.CARD_BANNER_RARE_L;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Nightingale.uniqueUsed = true;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new WFPPower(p), -1));
        if (upgraded) {
            for (AbstractPower power : p.powers)
                if (power.type == AbstractPower.PowerType.DEBUFF) {
                    if (power.amount > 1)
                        power.reducePower(1);
                    else
                        addToBot(new RemoveSpecificPowerAction(p, p, power));
                }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WhiteFiendProtection();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
        }
    }
}
