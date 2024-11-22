package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.actions.AnswerTheCallAction;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.AtkStance;

public class AvalancheBreaker extends AbstractFriendCard {
    public static final String ID = "nearlmod:AvalancheBreaker";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/avalanchebreaker.png";
    private static final int COST = 3;
    private static final int DAMAGE_AMT = 20;
    private static final int DAMAGE_UPG = 8;

    public AvalancheBreaker() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Gummy");
        magicNumber = baseMagicNumber = DAMAGE_AMT;
        bannerSmallRegion = ImageMaster.CARD_BANNER_UNCOMMON;
        bannerLargeRegion = ImageMaster.CARD_BANNER_UNCOMMON_L;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PureDamageAllEnemiesAction(p, magicNumber, belongFriend + damageSuffix));
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDeadOrEscaped()) {
                addToBot(new RemoveSpecificPowerAction(m, p, RegenPower.POWER_ID));
                addToBot(new RemoveSpecificPowerAction(m, p, StrengthPower.POWER_ID));
            }
        }
    }
    @Override
    public AbstractCard makeCopy() {
        return new AvalancheBreaker();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(DAMAGE_UPG);
        }
    }
}
