package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.NLMOD;
import nearlmod.orbs.Aurora;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

import static nearlmod.orbs.Aurora.status.RESPITE;

public class CheeseFondue extends AbstractNearlCard {
    public static final String ID = "nearlmod:CheeseFondue";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/cheesefondue.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 10;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public CheeseFondue() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        tags.add(NearlTags.FRIEND_RELATED);
        tags.add(NearlTags.IS_FOOD);
        belongFriend = Aurora.ORB_ID;
    }

    @Override
    public boolean extraTriggered() {
        if (NLMOD.checkOrb(Aurora.ORB_ID)) {
            for (AbstractOrb orb : AbstractDungeon.player.orbs)
                if (orb instanceof Aurora) {
                    return ((Aurora)orb).curStatus == RESPITE;
                }
        }
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        for (AbstractOrb o : p.orbs)
            if (o instanceof Aurora)
                ((Aurora) o).endRespite();
    }

    @Override
    public AbstractCard makeCopy() {
        return new CheeseFondue();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
