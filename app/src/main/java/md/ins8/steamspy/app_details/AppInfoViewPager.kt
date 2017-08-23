package md.ins8.steamspy.app_details

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_app_info.*
import kotlinx.android.synthetic.main.fragment_app_numbers.*
import kotlinx.android.synthetic.main.fragment_app_numbers_2.*
import kotlinx.android.synthetic.main.fragment_app_tags.*
import md.ins8.steamspy.R
import md.ins8.steamspy.RawSteamApp
import java.util.*

private val INFO_INDEX = 0
private val TAGS_INDEX = 1
private val NUMBERS_INDEX = 2
private val NUMBERS_2_INDEX = 3
private val TOTAL_PAGES = 4

private val GENERAL_INFO_EXTRA = "GeneralInfoExtra"
private val TAGS_EXTRA = "TagsExtra"
private val NUMBERS_EXTRA = "NumbersExtra"
private val NUMBERS_2_EXTRA = "Numbers2Extra"

private val PLUS_MINUS = "\u00B1"

data class GeneralAppInfo(
        val rank: Int,
        val price: String,
        val devs: String,
        val pubs: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    constructor(app: RawSteamApp) : this(
            app.rank,
            app.price,
            app.dev.joinToString(),
            app.pub.joinToString()
    )

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.apply {
            writeInt(rank)
            writeString(price)
            writeString(devs)
            writeString(pubs)
        }
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<GeneralAppInfo> {
        override fun createFromParcel(parcel: Parcel): GeneralAppInfo = GeneralAppInfo(parcel)

        override fun newArray(size: Int): Array<GeneralAppInfo?> = arrayOfNulls(size)
    }
}

data class AppNumbers(
        val numOwners: Int,
        val ownersVariance: Int,
        val playersTotal: Int,
        val playersTotalVariance: Int,
        val players2Weeks: Int,
        val players2WeeksVariance: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    constructor(app: RawSteamApp) : this(
            app.numOwners,
            app.ownersVariance,
            app.playersTotal,
            app.playersTotalVariance,
            app.players2Weeks,
            app.players2WeeksVariance
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(numOwners)
        parcel.writeInt(ownersVariance)
        parcel.writeInt(playersTotal)
        parcel.writeInt(playersTotalVariance)
        parcel.writeInt(players2Weeks)
        parcel.writeInt(players2WeeksVariance)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<AppNumbers> {
        override fun createFromParcel(parcel: Parcel): AppNumbers = AppNumbers(parcel)

        override fun newArray(size: Int): Array<AppNumbers?> = arrayOfNulls(size)
    }
}

data class AppNumbers2(
        val averageTotal: Int,
        val average2Weeks: Int,
        val medianTotal: Int,
        val median2Weeks: Int,
        val ccu: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    constructor(app: RawSteamApp) : this(
            app.averageTotal,
            app.average2Weeks,
            app.medianTotal,
            app.median2Weeks,
            app.ccu
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(averageTotal)
        parcel.writeInt(average2Weeks)
        parcel.writeInt(medianTotal)
        parcel.writeInt(median2Weeks)
        parcel.writeInt(ccu)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<AppNumbers2> {
        override fun createFromParcel(parcel: Parcel): AppNumbers2 = AppNumbers2(parcel)

        override fun newArray(size: Int): Array<AppNumbers2?> = arrayOfNulls(size)
    }

}

class GeneralInfoFragment : Fragment() {
    private var info: GeneralAppInfo? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        info = arguments.getParcelable(GENERAL_INFO_EXTRA)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_app_info, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rankText.text = "${info?.rank} %"
        priceText.text = "${info?.price} Units"
        developersText.text = info?.devs
        publishersText.text = info?.pubs
    }
}

class TagsFragment : Fragment() {
    private var tags: ArrayList<String>? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        tags = arguments.getStringArrayList(TAGS_EXTRA)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_app_tags, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tagGroup.setTags(tags)
    }
}

class NumbersFragment : Fragment() {
    private var numbers: AppNumbers? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        numbers = arguments.getParcelable(NUMBERS_EXTRA)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_app_numbers, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ownersNumber.text = formatDecimal(numbers?.numOwners)
        ownersVariance.text = "$PLUS_MINUS${formatDecimal(numbers?.ownersVariance)}"
        playersNumber.text = formatDecimal(numbers?.playersTotal)
        playersVariance.text = "$PLUS_MINUS${formatDecimal(numbers?.playersTotalVariance)}"
        players2WNumber.text = formatDecimal(numbers?.players2Weeks)
        players2WVariance.text = "$PLUS_MINUS${formatDecimal(numbers?.players2WeeksVariance)}"
    }
}

class Numbers2Fragment : Fragment() {
    private var numbers2: AppNumbers2? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        numbers2 = arguments.getParcelable(NUMBERS_2_EXTRA)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_app_numbers_2, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        averageNumber.text = formatDecimal(numbers2?.averageTotal)
        average2WNumber.text = formatDecimal(numbers2?.average2Weeks)
        medianNumber.text = formatDecimal(numbers2?.medianTotal)
        median2WNumber.text = formatDecimal(numbers2?.median2Weeks)
        ccuNumber.text = formatDecimal(numbers2?.ccu)
    }
}

class AppInfoPagerAdapter(fm: FragmentManager, private val context: Context, private val app: RawSteamApp) : FragmentPagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment = when (p0) {
        INFO_INDEX -> createGeneralInfoFragment()
        TAGS_INDEX -> createTagsFragment()
        NUMBERS_INDEX -> createNumbersFragment()
        NUMBERS_2_INDEX -> createNumbers2Fragment()
        else -> Fragment() // will never happen
    }

    override fun getCount(): Int = TOTAL_PAGES

    override fun getPageTitle(position: Int): CharSequence {
        val titleRes = when (position) {
            INFO_INDEX -> R.string.appDetailsViewPagerInfo
            TAGS_INDEX -> R.string.appDetailsViewPagerTags
            NUMBERS_INDEX -> R.string.appDetailsViewPagerNumbers
            NUMBERS_2_INDEX -> R.string.appDetailsViewPagerNumbers2
            else -> R.string.wtf
        }
        return context.getString(titleRes)
    }

    private fun createGeneralInfoFragment(): Fragment {
        val fragment = GeneralInfoFragment()
        val args = Bundle()
        args.putParcelable(GENERAL_INFO_EXTRA, GeneralAppInfo(app))
        fragment.arguments = args
        return fragment
    }

    private fun createTagsFragment(): Fragment {
        val fragment = TagsFragment()
        val args = Bundle()
        args.putStringArrayList(TAGS_EXTRA, app.tags.map { it.name } as ArrayList<String>?)
        fragment.arguments = args
        return fragment
    }

    private fun createNumbersFragment(): Fragment {
        val fragment = NumbersFragment()
        val args = Bundle()
        args.putParcelable(NUMBERS_EXTRA, AppNumbers(app))
        fragment.arguments = args
        return fragment
    }

    private fun createNumbers2Fragment(): Fragment {
        val fragment = Numbers2Fragment()
        val args = Bundle()
        args.putParcelable(NUMBERS_2_EXTRA, AppNumbers2(app))
        fragment.arguments = args
        return fragment
    }
}
