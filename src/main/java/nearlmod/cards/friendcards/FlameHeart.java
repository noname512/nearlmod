package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import nearlmod.actions.GainCostAction;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.LoseThornsPower;

import static nearlmod.patches.NearlTags.IS_KNIGHT_CARD;

public class FlameHeart extends AbstractFriendCard {
    public static final String ID = "nearlmod:FlameHeart";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/flameheart.png";
    private static final int COST = 1;
    private static final int COST_GAIN = 1;
    private static final int ATTACK_DMG = 7;
    private static final int THORN_GAIN = 6;

    public FlameHeart() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ENEMY, "nearlmod:Flametail");
        magicNumber = baseMagicNumber = ATTACK_DMG;
        secondMagicNumber = baseSecondMagicNumber = THORN_GAIN;
        tags.add(IS_KNIGHT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainCostAction(COST_GAIN));
        DamageInfo info = new DamageInfo(p, magicNumber);
        info.name = belongFriend + AbstractFriendCard.damageSuffix;
        addToBot(new DamageAction(m, info));
        addToBot(new ApplyPowerAction(p, p, new ThornsPower(p, secondMagicNumber)));
        addToBot(new ApplyPowerAction(p, p, new LoseThornsPower(p, secondMagicNumber)));
        if (upgraded)
            addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlameHeart();
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
