package com.nosae.coleader

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import com.nosae.coleader.base.BaseFragment
import com.nosae.coleader.base.BaseToolbarFragment
import com.nosae.coleader.base.ToolbarFragmentContainer
import com.nosae.coleader.data.CreateTeamEvent
import com.nosae.coleader.databinding.FragmentContactBinding
import com.nosae.coleader.utils.startActivity
import com.nosae.coleader.viewmodels.FriendsViewModel
import kotlinx.android.synthetic.main.fragment_contact.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ContactFragment : BaseToolbarFragment<FragmentContactBinding>() {

    private val viewModel by viewModels<FriendsViewModel> {
        FriendsViewModel.Factory()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_contact
    }

    override fun initView(b: FragmentContactBinding, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        bindToolbar("")
        b.viewModel = viewModel
        val adapter = PagerAdapter(childFragmentManager, listOf(
            FriendsListFragment(viewModel),
            TeamListFragment(viewModel)
        ))
        b.pager.adapter = adapter
        b.layoutTab.setViewPager(b.pager)

        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onCreateTeam(event: CreateTeamEvent) {
        viewModel.addTeam(event.newTeam)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_contact, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item_search -> {
                startActivity<NewFriendActivity>()
            }
            R.id.menu_item_create_team -> {
                startActivity<CreateActivity>()
            }
        }
        return true
    }

    class PagerAdapter(
        manager: FragmentManager,
        private val fragments: List<Fragment>
    ): FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }
}
