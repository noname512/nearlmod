package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Penance;
import nearlmod.patches.AbstractCardEnum;

public class LastWord extends AbstractFriendCard {
    public static final String ID = "nearlmod:LastWord";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/lastword.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 14;
    private static final int STRENGTH_LOSE = 99;
    private static final int UPGRADE_PLUS_DMG = 4;

    public LastWord() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ENEMY, "nearlmod:Penance");
        magicNumber = baseMagicNumber = ATTACK_DMG;
    }

    @Override
    public boolean extraTriggered() {
        return !Penance.penanceCardPlayedLastTurn;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo info = new DamageInfo(p, magicNumber);
        info.name = belongFriend + damageSuffix;
        addToBot(new DamageAction(m, info));
        if (extraTriggered()) {
            addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -STRENGTH_LOSE)));
            if (!m.hasPower("Artifact")) {
                addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, STRENGTH_LOSE)));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LastWord();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_DMG);
        }
    }
}
