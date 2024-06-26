package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.DefStance;

public class CounterattackWithShield extends AbstractNearlCard {
    public static final String ID = "nearlmod:CounterattackWithShield";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/counterattackwithshield.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 6;
    private static final int BLOCK_AMT = 6;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    public CounterattackWithShield() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        block = baseBlock = BLOCK_AMT;
    }
    
    @Override
    public boolean extraTriggered() {
        return AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        if (extraTriggered())
            addToBot(new GainBlockAction(p, block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CounterattackWithShield();
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
