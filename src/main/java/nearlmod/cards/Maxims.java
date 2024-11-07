package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.orbs.Horn;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

public class Maxims extends AbstractNearlCard {
    public static final String ID = "nearlmod:Maxims";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/maxims.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 4;
    private static final int BLOCK_AMT = 4;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    public Maxims() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        block = baseBlock = BLOCK_AMT;
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Horn.ORB_ID;
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Horn.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        addToBot(new GainBlockAction(p, block));
        if (extraTriggered()) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage)));
            addToBot(new GainBlockAction(p, block));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Maxims();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
