package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.NLMOD;
import nearlmod.orbs.Aurora;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

public class WarmthOfHome extends AbstractNearlCard {
    public static final String ID = "nearlmod:WarmthOfHome";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/warmthofhome.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 10;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public WarmthOfHome() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Aurora.ORB_ID;
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Aurora.ORB_ID);
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
        return new WarmthOfHome();
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
