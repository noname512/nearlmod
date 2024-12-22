package nearlmod.cards;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import nearlmod.NLMOD;
import nearlmod.actions.FallingShieldAction;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Horn;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FallingShield extends AbstractNearlCard {
    public static final String ID = "nearlmod:FallingShield";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/fallingshield.png";
    private static final int COST = 0;
    private static final int ATTACK_TIMES = 2;
    private static final int EXTRA_TIMES = 2;
    private static final int UPGRADE_PLUS_TIME = 1;
    private static final Logger logger = LogManager.getLogger(NLMOD.class.getName());

    public FallingShield() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = ATTACK_TIMES;
        secondMagicNumber = baseSecondMagicNumber = EXTRA_TIMES;
        tags.add(NearlTags.IS_SUMMON_CARD);
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Horn.ORB_ID;

        previewList = Nearl.getFriendCard(Horn.ORB_ID);
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Horn.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int times = magicNumber;
        baseDamage = TempHPField.tempHp.get(p);
        boolean summon = false;
        calculateCardDamage(m);
        if (extraTriggered()) {
            times += secondMagicNumber;
        } else {
            summon = true;
        }
        addToBot(new FallingShieldAction(damage, times, summon, m));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FallingShield();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_TIME);
            initializeDescription();
        }
    }
}
