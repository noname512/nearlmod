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

public class SilverlancePegasus extends AbstractNearlCard {
    public static final String ID = "nearlmod:SilverlancePegasus";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/silverlancepegasus.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 12;
    private static final int BLOCK_AMT = 10;
    private static final int UPGRADE_PLUS_DMG = 4;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public SilverlancePegasus() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.NONE);
        damage = baseDamage = ATTACK_DMG;
        block = baseBlock = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractMonster target = null;
        int maxIntent = -2;
        for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters)
            if (!ms.isDeadOrEscaped()) {
                int intent = ms.getIntentBaseDmg();
                if (intent != -1) {
                    intent = ms.getIntentDmg();
                }
                if (target == null || intent > maxIntent) {
                    target = ms;
                    maxIntent = intent;
                }
            }
        if (target != null)
            addToBot(new DamageAction(target, new DamageInfo(p, damage, damageTypeForTurn)));
        addToBot(new GainBlockAction(p, block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SilverlancePegasus();
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
