package nearlmod.cards.friendcards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import nearlmod.patches.AbstractCardEnum;

import static nearlmod.patches.NearlTags.IS_KNIGHT_CARD;

public class FeatherShineArrows extends AbstractFriendCard {
    public static final String ID = "nearlmod:FeatherShineArrows";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/feathershinearrows.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 17;
    private static final int UPGRADE_PLUS_DMG = 7;

    public FeatherShineArrows() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ALL_ENEMY, "nearlmod:Fartooth");
        magicNumber = baseMagicNumber = ATTACK_DMG;
        bannerSmallRegion = ImageMaster.CARD_BANNER_UNCOMMON;
        bannerLargeRegion = ImageMaster.CARD_BANNER_UNCOMMON_L;
        tags.add(IS_KNIGHT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractMonster target = null;
        for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters)
            if (!ms.isDeadOrEscaped() && (target == null || ms.currentHealth < target.currentHealth))
                target = ms;
        if (target != null) {
            DamageInfo info = new DamageInfo(p, magicNumber);
            info.name = belongFriend + AbstractFriendCard.damageSuffix;
            addToBot(new DamageAction(target, info));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FeatherShineArrows();
    }


    public void applyPowers() {
        super.applyPowers();
    }
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        applyPowers();
        AbstractMonster target = null;
        for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters)
            if (!ms.isDeadOrEscaped() && (target == null || ms.currentHealth < target.currentHealth))
                target = ms;
        if (target != null) {
            magicNumber = calculateSingleDamage(target, magicNumber, true);
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DMG);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderHand")
    public static class FeatherShineArrowsRenderPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"aliveMonsters"})
        public static void Insert(AbstractPlayer __instance, SpriteBatch sb, @ByRef int[] aliveMonsters) {
            if (__instance.hoveredCard instanceof FeatherShineArrows) {
                aliveMonsters[0] = 1;
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hoveredMonster");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }
}
