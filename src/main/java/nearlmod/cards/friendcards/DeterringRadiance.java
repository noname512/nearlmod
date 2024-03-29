package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;
import nearlmod.actions.WeakenAllAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.LightPower;

public class DeterringRadiance extends AbstractFriendCard {
    public static final String ID = "nearlmod:DeterringRadiance";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/deterringradiance.png";
    private static final int COST = 1;
    private static final int WEAK_CNT = 1;
    private static final int LIGHT_INC = 4;
    private static final int UPGRADE_PLUS_WEAK = 1;
    private static final int UPGRADE_PLUS_LIGHT = 2;

    public DeterringRadiance() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ALL_ENEMY, "nearlmod:Blemishine");
        magicNumber = baseMagicNumber = LIGHT_INC;
        secondMagicNumber = baseSecondMagicNumber = WEAK_CNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(p, new IntimidateEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 1.0F));
        addToBot(new WeakenAllAction(p, secondMagicNumber));
        addToBot(new ApplyPowerAction(p, p, new LightPower(p, magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DeterringRadiance();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_LIGHT);
            upgradeSecondMagicNumber(UPGRADE_PLUS_WEAK);
        }
    }
}
