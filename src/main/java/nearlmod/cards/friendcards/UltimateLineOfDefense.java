package nearlmod.cards.friendcards;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.patches.AbstractCardEnum;

public class UltimateLineOfDefense extends AbstractFriendCard {
    public static final String ID = "nearlmod:UltimateLineOfDefense";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/ultimatelineofdefense.png";
    private static final int COST = 3;
    private static final int ATTACK_DMG = 22;
    private static final int HP_LOSE = 10;
    private static final int UPGRADE_PLUS_ATK = 5;

    public UltimateLineOfDefense() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ALL_ENEMY, "nearlmod:Horn");
        magicNumber = baseMagicNumber = ATTACK_DMG;
        secondMagicNumber = baseSecondMagicNumber = HP_LOSE;
    }

    @Override
    public boolean extraTriggered() {
        int resHp = AbstractDungeon.player.currentHealth + TempHPField.tempHp.get(AbstractDungeon.player);
        return resHp > secondMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PureDamageAllEnemiesAction(p, magicNumber, belongFriend + damageSuffix));
        if (extraTriggered()) {
            addToBot(new LoseHPAction(p, p, secondMagicNumber));
            addToBot(new PureDamageAllEnemiesAction(p, magicNumber, belongFriend + damageSuffix));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UltimateLineOfDefense();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_ATK);
            initializeDescription();
        }
    }
}
